from flask import request
from flask_restful import Resource
from config import Config
from flask import request
from flask_restful import Resource

import boto3




class AWSTranslateKoResource(Resource) :

    # 대화 화면 한국어->외국어_ aws 번역 api
    def post(self):

        data = request.get_json()

        if 'language' not in data or 'answer' not in data :
            return{'result':'fail'}, 400
        
        if data['language'].strip() == '' or data['answer'].strip() == '':
            return {"result":"fail"}, 400
        
        
        answer = data['answer']

        language_map = {
                        '영어': 'en',
                        '일본어': 'ja',
                        '중국어(간체)': 'zh',
                        '스페인어' : 'es',
                        '프랑스어' : 'fr',
                        '독일어' : 'de',
                        '러시아어' : 'ru',
                        '힌디어' : 	'hi',
                        '우르두어' : 'ur',
                        '포르투갈어' : 'pt-PT'
                        }
        
        lag = language_map.get(data['language'], "")

        if lag == "":
            return {"result": "fail"}, 400
        
        transAnswer = self.translate(answer, lag)

        return {'result':'success', 'transAnswer':transAnswer}

    # 언어 선택 가이드 ('https://docs.aws.amazon.com/ko_kr/translate/latest/dg/what-is-languages.html')


    def translate(self, text, lag) :
        client = boto3.client(service_name='translate', 
                              region_name='ap-northeast-2',
                              aws_access_key_id = Config.AWS_TRANSLATE_ACCESS_KEY,
                              aws_secret_access_key = Config.AWS_TRANSLATE_SECRET_ACCESS_KEY)

        result = client.translate_text(Text=text, 
                                       SourceLanguageCode="ko", 
                                       TargetLanguageCode=lag)
        print('TranslatedText: ' + result.get('TranslatedText'))
        
        return result.get('TranslatedText')
    



class AWSTranslateForeignResource(Resource) :

    # 대화 화면 외국어->한국어_ aws 번역 api
    def post(self):

        data = request.get_json()

        if 'language' not in data or 'answer' not in data :
            return{'result':'fail'}, 400
        
        if data['language'].strip() == '' or data['answer'].strip() == '':
            return {"result":"fail"}, 400
        
        
        answer = data['answer']

        language_map = {
                        '영어': 'en',
                        '일본어': 'ja',
                        '중국어(간체)': 'zh',
                        '스페인어' : 'es',
                        '프랑스어' : 'fr',
                        '독일어' : 'de',
                        '러시아어' : 'ru',
                        '힌디어' : 	'hi',
                        '우르두어' : 'ur',
                        '포르투갈어' : 'pt-PT'
                        }
        
        lag = language_map.get(data['language'], "")

        if lag == "":
            return {"result": "fail"}, 400
        
        transAnswer = self.translate(answer, lag)

        return {'result':'success', 'transAnswer':transAnswer}


    def translate(self, text, lag) :
        client = boto3.client(service_name='translate', 
                              region_name='ap-northeast-2',
                              aws_access_key_id = Config.AWS_TRANSLATE_ACCESS_KEY,
                              aws_secret_access_key = Config.AWS_TRANSLATE_SECRET_ACCESS_KEY)

        result = client.translate_text(Text=text, 
                                       SourceLanguageCode=lag, 
                                       TargetLanguageCode="ko")
        print('TranslatedText: ' + result.get('TranslatedText'))
        
        return result.get('TranslatedText')


