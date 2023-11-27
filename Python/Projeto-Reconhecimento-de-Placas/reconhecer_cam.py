import cv2
import pytesseract
import numpy as np
import re
import tkinter as tk
from tkinter import simpledialog, filedialog
from PIL import ImageTk, Image
import os
import uuid
from datetime import datetime
import time  # Importe a biblioteca time

pytesseract.pytesseract.tesseract_cmd = 'C:/Program Files/Tesseract-OCR/tesseract.exe'

plate_cascade = cv2.CascadeClassifier('C:\\Users\\Angelo\\AppData\\Local\\Programs\\Python\\Python311\\Lib\\site-packages\\cv2\\data\\haarcascade_russian_plate_number.xml')

def preprocess_plate(image):
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    blurred = cv2.GaussianBlur(gray, (5, 5), 0)
    return blurred

def detect_plates(image, saved_plates, saved_weights, last_detection_time):
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    plates = plate_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(80, 25))

    for (x, y, w, h) in plates:
        # Adicione o mecanismo de intervalo de 5 segundos
        current_time = time.time()
        if current_time - last_detection_time < 5:
            continue  # Aguarde 5 segundos antes de permitir outra detecção

        plate_img = image[y:y + h, x:x + w]

        processed_plate = preprocess_plate(plate_img)

        plate_text = pytesseract.image_to_string(processed_plate, config='--psm 7')

        texto_completo = plate_text

        padrao = r'[A-Z]{3}[0-9]{1}[A-Z]{1}[0-9]{2}'
        ocorrencias = re.findall(padrao, texto_completo)

        if ocorrencias:
            placa_encontrada = ocorrencias[0]
            hora_atual = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
            print("Placa encontrada:", placa_encontrada)

            peso = simpledialog.askfloat("Inserir Peso", "Insira o peso do veículo (em kg):")

            with open('logs.txt', 'a') as file:
                file.write(f"Placa: {placa_encontrada}, Hora: {hora_atual}, Peso: {peso}\n")

            if placa_encontrada in saved_plates:
                index = np.where(saved_plates == placa_encontrada)
                peso_anterior = saved_weights[index][0]
                diff = peso - peso_anterior
                print("Placa já existente. Diferença de pesos:", diff)
                saved_plates = np.delete(saved_plates, index)
                saved_weights = np.delete(saved_weights, index)

            else:
                saved_plates = np.append(saved_plates, placa_encontrada)
                saved_weights = np.append(saved_weights, peso)

                image_name = str(uuid.uuid4()) + ".jpg"
                image_path = os.path.join("imagens", image_name)
                cv2.imwrite(image_path, plate_img)
                print("Imagem salva:", image_path)

            print("Peso do veículo:", peso)

            last_detection_time = current_time  # Atualize o tempo da última detecção

        else:
            print("Nenhuma placa encontrada no texto.")

        cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)

    return image, saved_plates, saved_weights, last_detection_time

def start_detection(source_type):
    cap = None
    if source_type == 'camera':
        cap = cv2.VideoCapture(0)
    elif source_type == 'video':
        file_path = filedialog.askopenfilename(title="Selecione um vídeo", filetypes=[("Arquivos de vídeo", "*.mp4;*.avi")])
        if not file_path:
            return
        cap = cv2.VideoCapture(file_path)

    saved_plates = np.array([])
    saved_weights = np.array([])
    last_detection_time = 0  # Adicione uma variável para rastrear o tempo da última detecção

    while True:
        ret, frame = cap.read()

        if not ret:
            break

        processed_frame, saved_plates, saved_weights, last_detection_time = detect_plates(frame, saved_plates, saved_weights, last_detection_time)

        cv2.imshow("Reconhecimento de Placas", processed_frame)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

    window.quit()
    window.destroy()

# Criação da janela principal
window = tk.Tk()
window.title("Reconhecimento de Placas")
window.geometry("800x600")

# Carrega a imagem de fundo
background_image = Image.open("imagens\\background.jpg")
background_photo = ImageTk.PhotoImage(background_image)
background_label = tk.Label(window, image=background_photo)
background_label.place(x=0, y=0, relwidth=1, relheight=1)

# Adiciona um cabeçalho de texto na imagem de fundo
header_label = tk.Label(window, text="Sistema de Reconhecimento de Placas de Veículos", font=("Arial", 24), bg="white")
header_label.pack(pady=20)

# Criação do botão "Iniciar Reconhecimento" com tamanho maior
startbutton = tk.Button(window, text="Iniciar Reconhecimento", command=lambda: start_detection(source_var.get()), font=("Arial", 18), width=20, height=2)
startbutton.pack(pady=50)

# Adiciona um menu de opções para selecionar a fonte
source_var = tk.StringVar(window)
source_var.set("camera")  # Valor padrão
source_menu = tk.OptionMenu(window, source_var, "camera", "video")
source_menu.pack(pady=20)

# Função para fechar a janela
def close_window():
    window.quit()
    window.destroy()

# Configurar a ação de fechar a janela
window.protocol("WM_DELETE_WINDOW", close_window)

# Executa a janela principal
window.mainloop()
