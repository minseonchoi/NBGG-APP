from bs4 import BeautifulSoup
from flask import json, request
from flask_restful import Resource
import requests
from mysql_connection import get_connection
from mysql.connector import Error
from config import Config
import xml.etree.ElementTree as ET
import html





class CountrySearchListResource(Resource):

    # 국가 검색창 API
    def get(self):

        # DB로부터 데이터를 가져옴
        try :
            connection = get_connection()
            
            query = '''
                    select koreanName, englishName, flagImageUrl
                    from country;
                    '''

            cursor = connection.cursor(dictionary=True)

            cursor.execute( query )

            # 데이터를 가져와야한다.
            result_list = cursor.fetchall()

            # 튜플로는 보낼수 없다 그래서 커서에 dictionary=True 추가
            print(result_list)

            cursor.close()
            connection.close()
        
        except Error as e:
            if cursor is not None :
                cursor.close()
            if connection is not None :
                connection.close()
            return {'result':'fail', 'error':str(e)}, 500
        
        # 클라이언트에게 json만들어서 응답

        print(result_list)

        return {'countries' : result_list,
                'result' : 'success'}

    

class EntryPermitRequirements(Resource):

    # 입국 허가 요건 API
    def post(self):
        
        # 사용자에게 국가이름 받기
        data = request.get_json()

        if 'countryName' not in data :
            return{'result':'fail'}, 400
        
        if data['countryName'].strip() == '' :
            return {"result":"fail"}, 400

        countryName = data.get('countryName')

        # 외교부 입국 허가 요건 api 호출

        url = "http://apis.data.go.kr/1262000/EntranceVisaService2/getEntranceVisaList2"
        params = {"serviceKey" : Config.MINISTRY_OF_FOREIGN_AFFAIRS_SERVICE_KEY,
                  "cond[country_nm::EQ]" : countryName}

        response = requests.get(url=url, params=params)
        
        if response.status_code == 200:
            response = response.json()
            print(response['data'])

            # 필요한 필드들만 추출
            filtered_data = []
            for item in response['data']:
                filtered_item = {
                    "countryName": item.get("country_nm"),   # 국가명
                    "countryEngName": item.get("country_eng_nm"), # 국가 영문명
                    "haveYn": item.get("have_yn"), # 여권소지여부
                    "gnrlPsptVisaYn": item.get("gnrl_pspt_visa_yn"), # 일반여권 입국허가요건 여부
                    "gnrlPsptVisaCn": item.get("gnrl_pspt_visa_cn"), # 일반여권 입국허가요건 내용
                    "ofclpsptVisaYn": item.get("ofclpspt_visa_yn"), # 관용여권 입국허가요건 여부
                    "ofclpsptVisaCn": item.get("ofclpspt_visa_cn"), # 관용여권 입국허가요건 내용
                    "dplmtPsptVisaYn": item.get("dplmt_pspt_visa_yn"), # 외교관여권 입국허가요건 여부
                    "dplmtPsptVisaCn": item.get("dplmt_pspt_visa_cn"), # 외교관여권 입국허가요건 내용
                    "nvisaEntryEvdcCn": item.get("nvisa_entry_evdc_cn"), # 무비자 입국근거내용
                    "remark": item.get("remark") # 비고 
                }
                filtered_data.append(filtered_item)

            return {
                "result": "success",
                "entryPermitRequirements": filtered_data
            }
        else:
            return {
                "result": "fail",
                "message": f"API request failed with status code {response.status_code}"
            }


class LocalContactResource(Resource):
    
    # 국가별 연락처(현지대사관) api
    def post(self):

        countryData = request.get_json()

        if 'countryName' not in countryData :
            return{'result':'fail'}, 400
        
        if countryData['countryName'].strip() == '' :
            return {"result":"fail"}, 400

        service_key = Config.MINISTRY_OF_FOREIGN_AFFAIRS_SERVICE_KEY

        url = 'http://apis.data.go.kr/1262000/LocalContactService2/getLocalContactList2'
        params = {
            'serviceKey': service_key,
            'numOfRows': '10', 
            'cond[country_nm::EQ]': countryData['countryName'], 
            'pageNo': '1'
        }

        response = requests.get(url, params=params)

        data = response.content  # 응답 내용을 바이트로 변환

        text_data = data.decode('utf-8')  # 바이트를 문자열로 디코딩

        json_data = json.loads(text_data)  # 문자열을 JSON 데이터로 파싱

        strip_data = json_data['data'][0]['contact_remark']
        img_url = json_data['data'][0]['flag_download_url']

        # HTML 파싱
        soup = BeautifulSoup(strip_data, 'html.parser')
        soup.title

        textData = soup.get_text()

        print(textData)

        return {'result': 'success', 'localContact': textData, 'flagUrl':img_url}
    


class CountryBasicInformation(Resource):

    # 국가 기본 정보 api
    def post(self):

        # 사용자에게 국가 이름 받기
        data = request.get_json()

        if 'countryName' not in data :
            return{'result':'fail'}, 400
        
        if data['countryName'].strip() == '' :
            return {"result":"fail"}, 400

        countryName = data.get('countryName')

        url = "http://apis.data.go.kr/1262000/CountryBasicService/getCountryBasicList"
        params = {
            "serviceKey": Config.MINISTRY_OF_FOREIGN_AFFAIRS_SERVICE_KEY,
            "numOfRows": '10', 
            "countryName": countryName, 
            "pageNo": '1'
        }

        response = requests.get(url=url, params=params)

        if response.status_code == 200:
            try:
                # XML 응답 파싱
                root = ET.fromstring(response.content)
                
                # 필요한 데이터 추출
                basic_info = root.find('.//item/basic').text
                
                # 특수문자 및 HTML 인코딩을 디코딩
                basic_info = html.unescape(basic_info)

                # HTML 태그 제거
                soup = BeautifulSoup(basic_info, 'html.parser')
                clean_text = soup.get_text()

                return {
                    "result": "success",
                    "countryBasicInformation": clean_text
                }
            except ET.ParseError:
                return {
                    "result": "failure",
                    "message": "Failed to parse XML response"
                }
        else:
            return {
                "result": "failure",
                "message": f"API request failed with status code {response.status_code}"
            }



class TravelAlarm(Resource):

    # 여행 경보 api
    def post(self):

        # 사용자에게 국가이름 받기
        data = request.get_json()

        if 'countryName' not in data :
            return{'result':'fail'}, 400
        
        if data['countryName'].strip() == '' :
            return {"result":"fail"}, 400

        countryName = data.get('countryName')

        # 외교부 입국 허가 요건 api 호출

        url = "http://apis.data.go.kr/1262000/TravelAlarmService2/getTravelAlarmList2"
        params = {"serviceKey" : Config.MINISTRY_OF_FOREIGN_AFFAIRS_SERVICE_KEY,
                  "cond[country_nm::EQ]" : countryName,
                  "numOfRows" : 1,
                  "pageNo" : 1,
                  "returnType" : "JSON"}

        response = requests.get(url=url, params=params)

        if response.status_code == 200:
            response = response.json()
            print(response['data'])

            # 필요한 필드들만 추출
            filtered_data = []
            for item in response['data']:
                filtered_item = {
                    "countryName": item.get("country_nm"),   # 국가명
                    "countryEngName": item.get("country_eng_nm"), # 국가 영문명
                    "continentName": item.get("continent_nm"), # 대륙명
                    "dangMapUrl": item.get("dang_map_download_url"), # 위험지역 지도 경로 사진 url
                    "mapUrl": item.get("map_download_url"), # 위험지도 사진 url
                    "flagUrl": item.get("flag_download_url"), # 국기사진 url
                }
                filtered_data.append(filtered_item)

            return {
                "result" : "success",
                "travelAlarm" : filtered_item
            }

        else:
            return {
                "result": "fail",
                "message": f"API request failed with status code {response.status_code}"
            }
