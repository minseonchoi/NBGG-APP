from bs4 import BeautifulSoup
from flask import json, jsonify, request
from flask_restful import Resource
from config import Config
import requests
import google.generativeai as genai


class WeatherResource(Resource):
    
    # 날씨 api
    def post(self):

        locationData = request.get_json()
        if 'lat' not in locationData or 'lon' not in locationData :
            return{'result':'fail'}, 400

        service_key = Config.OPEN_WEATHER_KEY

        url = 'https://api.openweathermap.org/data/2.5/weather'
        params = {
            'lat': locationData['lat'],
            'lon': locationData['lon'],
            'lang': 'kr',
            'units':'metric',
            'appid': service_key
        }

        response = requests.get(url, params=params)

        
        # JSON 데이터 파싱
        data = response.json()

        # 날씨 상태
        weather = data.get('weather', [])[0]
        description = weather.get('description')
        iconN = weather.get('icon')

        icon = 'https://openweathermap.org/img/wn/'+iconN+'@4x.png'

        # 온도
        main = data.get('main')
        temp = main.get('temp') # 현재온도
        tempMin = main.get('temp_min') # 최저온도
        tempMax = main.get('temp_max') # 최고온도
        feelsLike = main.get('feels_like') # 체감온도
        humidity = main.get('humidity') # 습도

        clouds = data.get('clouds', {}).get('all', 0) # 예상 강수량
        


        return {'result': 'success', 
                'description':description, 
                'icon':icon, 
                'temp':temp,
                'tempMin':tempMin,
                'tempMax':tempMax,
                'feelsLike':feelsLike,
                'humidity':humidity,
                'clouds':clouds }
    



class GeminiResource(Resource):

    # gemini를 활용한 날씨 비교 분석 api
    def post(self):
        
        twoWeather = request.get_json()

        if 'currentDescription' not in twoWeather or 'currentTemp' not in twoWeather or 'currentHumidity' not in twoWeather or 'currentClouds' not in twoWeather or 'searchDescription' not in twoWeather or 'searchTemp' not in twoWeather or 'searchHumidity' not in twoWeather or 'searchClouds' not in twoWeather:
            return {'result': 'fail'}, 400


        GOOGLE_API_KEY = Config.GOOGLE_API_KEY

        genai.configure(api_key=GOOGLE_API_KEY)

        # Create the model
        generation_config = {
                            "temperature": 0.3,
                            "top_p": 0.95,
                            "top_k": 64,
                            "max_output_tokens": 8192,
                            "response_mime_type": "text/plain",
                            }

        model = genai.GenerativeModel(
                                      model_name="gemini-1.5-flash",
                                      generation_config=generation_config
                                      )
        # 이전 대화 넣는 코드
        # chat_session = model.start_chat(history=[])
        # response = chat_session.send_message("안녕하세요 무엇을 도와드릴까요?")

        content = "currentDescription : "+ twoWeather['currentDescription']\
        +" ,"+"currentTemp : "+ str(twoWeather['currentTemp'])\
        +" ,"+"currentHumidity : "+ str(twoWeather['currentHumidity'])\
        +" ,"+"currentClouds : "+ str(twoWeather['currentClouds']) + "%"\
        +" ,"+"searchDescription : "+ twoWeather['searchDescription']\
        +" ,"+"searchTemp : "+ str(twoWeather['searchTemp'])\
        +" ,"+"searchHumidity : "+ str(twoWeather['searchHumidity'])\
        +" ,"+"searchClouds : "+ str(twoWeather['searchClouds']) + "%"\
        +" current가 들어간것은 현재 위치 날씨고, search가 들어간 것은 검색위치 날씨야. 둘의 차이점을 제공된 정보만 비교하고 분석하고 다른 말을 하지마. 추가로 날씨 관련해서 추천해줄만한 정보 부탁해."


        response = model.generate_content(content)

        html_content = response.text

        soup = BeautifulSoup(html_content, 'html.parser')
        soup.title

        textData = soup.get_text()

        if "\\n" in textData :
            textData = textData.replace('\\n','\n')

        if "##" in textData :
            textData = textData.replace('##','')

        return{"result":"success", "response":textData}
