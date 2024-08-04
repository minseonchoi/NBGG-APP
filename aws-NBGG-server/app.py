from flask import Flask
from config import Config
from flask_restful import Api
import serverless_wsgi
from resources.country import CountryBasicInformation, CountrySearchListResource, EntryPermitRequirements, LocalContactResource, TravelAlarm
from resources.immigration import QuestionAnswerList
from resources.translate import AWSTranslateForeignResource, AWSTranslateKoResource
from resources.weather import GeminiResource, WeatherResource

app = Flask(__name__)

# 환경변수 셋팅
app.config.from_object(Config)

api = Api(app)

# country
api.add_resource(CountrySearchListResource, '/country')
api.add_resource(LocalContactResource, '/country/contack')
api.add_resource(EntryPermitRequirements, '/country/entry')
api.add_resource(CountryBasicInformation, '/country/basicInformation')
api.add_resource(TravelAlarm, "/country/travelAlarm")

# wheather
api.add_resource(WeatherResource, '/weather/location')
api.add_resource(GeminiResource, '/weather/gemini')

# translate
api.add_resource(AWSTranslateKoResource, '/translate/aws')
api.add_resource(AWSTranslateForeignResource, '/translate/foreign/aws')

# immigration
api.add_resource(QuestionAnswerList, '/immigration')



def handler(event, context):
    return serverless_wsgi.handle_request(app, event, context)


if __name__ == '__main__':
    app.run()