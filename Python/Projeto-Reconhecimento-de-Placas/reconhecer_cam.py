import cv2
import pytesseract
import numpy as np
import re
import tkinter as tk
from tkinter import simpledialog
import os
import uuid
from datetime import datetime

pytesseract.pytesseract.tesseract_cmd = 'C:/Program Files/Tesseract-OCR/tesseract.exe'

plate_cascade = cv2.CascadeClassifier('C:\\Users\\cristian.kazimirski\\AppData\\Local\\Programs\\Python\\Python312\\Lib\\site-packages\\cv2\\data\\haarcascade_russian_plate_number.xml')

def preprocess_plate(image):
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    blurred = cv2.GaussianBlur(gray, (5, 5), 0)
    return blurred

def detect_plates(image, saved_plates, saved_weights):
    gray = cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
    plates = plate_cascade.detectMultiScale(gray, scaleFactor=1.1, minNeighbors=5, minSize=(80, 25))

    for (x, y, w, h) in plates:
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

            root = tk.Tk()
            root.withdraw()
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

        else:
            print("Nenhuma placa encontrada no texto.")

        cv2.rectangle(image, (x, y), (x + w, y + h), (0, 255, 0), 2)

    return image, saved_plates, saved_weights

def main():
    cap = cv2.VideoCapture(0)

    saved_plates = np.array([])
    saved_weights = np.array([])

    while True:
        ret, frame = cap.read()

        if not ret:
            break

        processed_frame, saved_plates, saved_weights = detect_plates(frame, saved_plates, saved_weights)

        cv2.imshow("License Plate Detection", processed_frame)

        if cv2.waitKey(1) & 0xFF == ord('q'):
            break

    cap.release()
    cv2.destroyAllWindows()

if __name__ == '__main__':
    main()