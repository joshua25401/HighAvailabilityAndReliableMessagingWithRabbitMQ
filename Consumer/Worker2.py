import pika
import json
import Constant
import random
from pika import channel
from pika.exchange_type import ExchangeType
from pika.exceptions import *

# Define Credentials
credentials = pika.PlainCredentials("guest", "guest")

# Define multiple parameters
all_endpoint = [
    pika.ConnectionParameters(
        "localhost", "5672", credentials=credentials, connection_attempts=5, retry_delay=1),
]

# Message Callback


def callback(
        ch,
        method,
        properties,
        body):
    data = json.loads(body)
    print("[*RECEIVED*] %s => %s" % (method.routing_key, data))
    print("[*DEMO_SAVING_TO_DB*] %s => %s" % (method.routing_key, data))
    ch.basic_ack(delivery_tag=method.delivery_tag)


while(True):
    try:
        random.shuffle(all_endpoint)
        connection = pika.BlockingConnection(all_endpoint)
        channel = connection.channel()

        # Define the Maximum Consumer Message Processed / Consume
        channel.basic_qos(prefetch_count=1)

        # Declare the Exchange
        channel.exchange_declare(
            exchange=Constant.EXCHANGE_NAME, exchange_type=ExchangeType.direct, durable=True)

        # Declare Queue that Consumer want to Consume
        channel.queue_declare(queue=Constant.QUEUE_NAMES[0], durable=True)
        channel.queue_declare(queue=Constant.QUEUE_NAMES[1], durable=True)

        # Binding Exchange with Queue and Routing Key
        channel.queue_bind(
            exchange=Constant.EXCHANGE_NAME,
            queue=Constant.QUEUE_NAMES[0], routing_key=Constant.ROUTING_KEYS[0])
        channel.queue_bind(
            exchange=Constant.EXCHANGE_NAME,
            queue=Constant.QUEUE_NAMES[1], routing_key=Constant.ROUTING_KEYS[1])

        # Start the basic consume with auto_ack = False (Manual Ack) for each Queue
        channel.basic_consume(
            queue=Constant.QUEUE_NAMES[0], on_message_callback=callback, auto_ack=False
        )
        channel.basic_consume(
            queue=Constant.QUEUE_NAMES[1], on_message_callback=callback, auto_ack=False
        )

        try:
            print("[*CONNECTED*] Connected! Consumer waiting for messages!")
            channel.start_consuming()
        except KeyboardInterrupt:
            channel.stop_consuming()
            print("[*STOPPED*] Keyboard Interrupt! Consumer has stopped consuming :D")
            connection.close()
            break

    except ConnectionClosedByBroker:
        continue
    except pika.exceptions.AMQPConnectionError:
        print("[*CONNECTION_BROKEN*] Re-trying to connect.. !!")
        continue
