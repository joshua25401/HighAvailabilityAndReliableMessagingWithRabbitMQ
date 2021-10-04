FROM python:latest

WORKDIR /worker-app

COPY requirement.txt .

COPY ./Worker ./Worker

RUN pip install -r requirement.txt

CMD [ "python", "./Worker/Worker.py" ]