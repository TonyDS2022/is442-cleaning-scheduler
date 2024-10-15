import requests
import os
import dotenv

PATH = os.path.join("src", "main", "resources", ".env")
dotenv.load_dotenv(PATH)
      
url = "https://www.onemap.gov.sg/api/auth/post/getToken"
      
payload = {
        "email": os.getenv('ONEMAP_EMAIL'),
        "password": os.getenv('ONEMAP_PASSWORD'),
}
      
response = requests.request("POST", url, json=payload)
      
print(response.text)