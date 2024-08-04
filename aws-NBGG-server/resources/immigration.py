from aifc import Error
from flask_restful import Resource

from mysql_connection import get_connection


class QuestionAnswerList(Resource):

    # 입국 심사 질문과 답변 리스트 가져오는 API
    def get(self):
        # DB로부터 데이터를 가져옴
        try :
            connection = get_connection()
            
            query = '''
                    select id, questionEng, questionKor, answerEng, answerKor
                    from questionAnswer;
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

        return {'questionAnswer' : result_list,
                'result' : 'success'}