import os
import uvicorn
from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from langchain_google_genai import GoogleGenerativeAIEmbeddings, ChatGoogleGenerativeAI

# 1. 환경 설정 및 API 키 로드
#. .env 파일에서 환경 변수 불러오기
load_dotenv()

# API 키 로드 확인
api_key = os.getenv("GOOGLE_API_KEY")
if not api_key:
    raise ValueError("❌[오류] /env 파일이 없거나 GOOGLE_API_KEY가 설정되지 않았습니다.")

# 2. FastAPI 앱 및 AI 모델 초기화
app = FastAPI()

print("⏳ Gemini AI 엔진 시동 중...")

try:
    # 임베딩 모델 (텍스트 -> 벡터 변환용)
    # DB의 vector(768) 컬럼과 호환되는 768차원 모델 사용
    embeddings = GoogleGenerativeAIEmbeddings(model="models/text-embedding-004")
    
    # LLLM 모델 (채팅 및 답변 생성용)
    # gemini-1.5 flash 사용
    llm = ChatGoogleGenerativeAI(model="gemini-1.5-flash")
    
    print("✅ Gemini AI 준비 완료!")
    
except Exception as e:
    print(f"❌모델 초기화 실패: {e}")
    raise e


# 3. 데이터 통신 규격(DTO) 정의
class EmbedRequest(BaseModel):
    text: str

class ChatRequest(BaseModel):
    question: str
    context: str = "" # 겸색된 일기 내용
    

# 4. API 엔드포인트 정의
@app.post("/embed")
async def create_embedding(request: EmbedRequest):
    """
    [Java -> Python] 일기 저장 시 호출
    텍스트를 받아 768차원 벡터 리스트로 변환하여 반환
    """
    if not request.text:
        raise HTTPException(status_code=400, detail="텍스트가 비어 있습니다.")
    
    try: 
        # Langchain을 통해 Google 서버에 임베딩 요청
        vector = embeddings.embed_query(request.text)
        return {"embedding" : vector}
    
    except Exception as e:
        print(f"❌임베딩 생성 중 오류 발생: {e}")
        raise HTTPException(status_code=500, detail=str(e))
    
@app.post("/chat")
async def chat_with_ai(request: ChatRequest):
    """
    [Java -> Python] 채팅 시 호출
    질문과 관련된 일기(context)를 받아 AI 답변 생성
    """
    
    try: 
        # 프롬프트 구성
        prompt = f"""
        너는 사용자의 일기를 기억하고 공감해주는 따뜻한 AI '소곤'이야.
        이래 제공된 [관련 일기] 내용을 바탕으로 사용자의 [질문]에 답변해줘.
        일기 내용에 없는 정보라면 솔직하게 모른다고 답변하고, 항상 친근한 말투
        (해요체)를 사용하도록 해. 
        
        [관련 일기]:
        {request.context}
        
        [질문]:
        {request.question}
        
        답변:
        """
        
        # LLM에게 질문 전송
        response = llm.invoke(prompt)
        return {"answer": response.content}
    
    except Exception as e:
        print(f"❌답변 생성 중 오류 발생: {e}")
        raise HTTPException(status_code=500, detail=str(e))
    
    # 5. 서버 실행
if __name__ == "__main__":
    # 0.0.0.0은 외부 접속 허용, 포트 8000번 사용
    uvicorn.run(app, host="0.0.0.0", port = 8000)