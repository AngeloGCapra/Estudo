from twilio.rest import Client
import os
import dotenv
import time
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.by import By
from selenium.webdriver.support import expected_conditions as EC

meta_preco = '140'

servico = Service(ChromeDriverManager().install())
navegador = webdriver.Chrome(service=servico)
navegador.maximize_window()

navegador.get(r"https://www.noticiasagricolas.com.br/cotacoes/soja")
preco_atual = navegador.find_elements(By.XPATH, '/html/body/div[1]/div[4]/section/div[3]/div[2]/div[1]/div[2]/table/tbody/tr/td[2]')[0].text

print(preco_atual)

def enviar_sms(preco_atual):
    dotenv.load_dotenv(dotenv.find_dotenv())

    REMETENTE = os.getenv('REMETENTE')
    DESTINATARIO = os.getenv('DESTINATARIO')
    ACCOUNT_SID = os.getenv('ACCOUNT_SID')
    AUTH_TOKEN = os.getenv('AUTH_TOKEN')

    client = Client(ACCOUNT_SID, AUTH_TOKEN)

    message = client.messages.create(
        body = f'Soja está por R${preco_atual}',
        from_ = REMETENTE,
        to = DESTINATARIO
    )

    return message.sid

if preco_atual >= meta_preco:
    print(enviar_sms(preco_atual))
