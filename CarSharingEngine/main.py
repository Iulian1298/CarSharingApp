import time
from threading import Thread

from Mqtt import Mqtt

usersDriverRequest = {}


def performUserFilter(position, user):
    print(position)
    print(user)
    pass


def on_message(client, userdata, a_message):
    global usersDriverRequest
    print("Topic: " + str(a_message.topic) + " Message: " + a_message.payload.decode("utf-8"))
    msg = a_message.payload.decode("utf-8")
    topic = a_message.topic
    if topic == "/findDriver":
        usersDriverRequest[msg] = "not assigned"
        client.publish("/getPosition", "get")
    if topic == "/position":
        position, user = msg.split(" ")
        performUserFilter(position, user)


def main():
    mqtt = Mqtt(("127.0.0.1", 1884, 60), on_message, [("/position", 0), ("/findDriver", 0)])
    try:
        mqttThread = Thread(target=mqtt.listen)
        mqttThread.start()
        mqttThread.join()
    except KeyboardInterrupt:
        print("Keyboard break!")


if __name__ == '__main__':
    main()
