FROM python:3.9-slim-bullseye

RUN mkdir -p /usr/src/app
RUN mkdir -p /usr/src/app/sqlite
RUN chmod 777 /usr/src/app/sqlite
WORKDIR /usr/src/app

COPY deployment/requirements.txt /usr/src/app/
RUN pip install --no-cache-dir -r requirements.txt

COPY src/main/python /usr/src/app

# Expose the Flask port
EXPOSE 5000

CMD [ "python", "./start.py" ]
