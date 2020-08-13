import paho.mqtt.client as mqtt
import time


class Mqtt:
    def __init__(self, brokerAddr, on_message, topics):
        self.m_client = mqtt.Client()
        self.m_client.on_message = on_message
        self.m_client.on_connect = self.on_connect
        self.m_client.connect(*brokerAddr)
        self.m_client.subscribe(topics)
        print("Connection established and subscribed to topics: " + str([i[0] for i in topics]))

    def listen(self):
        print("Enter in loop mode, wait incoming connection.. ")
        self.m_client.loop_forever()

    @staticmethod
    def on_connect(client, userdata, flags, rc):
        print("Connected with result code " + str(rc))

    def subscribe(self, topics):
        self.m_client.subscribe(topics)

    def publish(self, topic, message):
        self.m_client.publish(topic, message)
