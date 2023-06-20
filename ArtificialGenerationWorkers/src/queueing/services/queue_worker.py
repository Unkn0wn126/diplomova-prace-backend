import pika, json, sys, os

# basic skeleton taken from: https://www.rabbitmq.com/tutorials/tutorial-one-python.html
def main():
    connection = pika.BlockingConnection(pika.ConnectionParameters('artificial-generation-rabbitmq', credentials=pika.credentials.PlainCredentials('myuser', 'mypassword')))
    channel = connection.channel()

    channel.queue_declare(queue='spring-boot')

    channel2 = connection.channel()

    channel2.queue_declare(queue='python')

    def callback(ch, method, properties, body):
        parsed = body.decode('utf8')
        loaded = json.loads(parsed)
        client_id = loaded.get('clientID')
        operation_id = loaded.get('operationID')
        operation_type = loaded.get('operationType')
        content = loaded.get('content')

    channel.basic_consume(queue='spring-boot',
                          auto_ack=True,
                          on_message_callback=callback)

    print(' [*] Waiting for messages. To exit press CTRL+C')
    channel.start_consuming()


if __name__ == '__main__':
    try:
        main()
    except KeyboardInterrupt:
        print('Interrupted')
        try:
            sys.exit(0)
        except SystemExit:
            os._exit(0)
