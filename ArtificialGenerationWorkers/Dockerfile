# syntax=docker/dockerfile:1

FROM python:3.11.3-bullseye

RUN mkdir app
RUN mkdir /app/src

COPY requirements.txt /app/requirements.txt
RUN pip3 install -r /app/requirements.txt

COPY ./src /app/src

ENV PYTHONPATH "${PYTHONPATH}:/app/src"

ENTRYPOINT ["python", "/app/src/artificial_generation.py"]

#CMD ["python", "/app/src/artificial_generation.py"]