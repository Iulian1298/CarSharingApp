
import time

# Define the callback to handle CONNACK from the broker, if the connection created normal, the value of rc is 0
def on_connect(client, userdata, flags, rc):
    print("Connection returned with result code:" + str(rc))

# Callback handles disconnection, print the rc value
def on_disconnect(client, userdata, rc):
    print("Disconnection returned result:"+ str(rc))

import paho.mqtt.client as paho

def on_subscribe(client, userdata, mid, granted_qos):
    print("Subscribed: "+str(mid)+" "+str(granted_qos))

def on_message(client, userdata, msg):
    print(msg.topic+" "+str(msg.qos)+" "+str(msg.payload))

client = paho.Client()
client.on_subscribe = on_subscribe
client.on_message = on_message
client.connect("127.0.0.1", 1884)
client.subscribe([("/position", 0), ("/findDriver", 0)])



client.loop_forever()
