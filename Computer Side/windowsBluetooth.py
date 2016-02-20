import keyboardEvents
from bluetooth import *

server_sock=BluetoothSocket( RFCOMM )
server_sock.bind(("",PORT_ANY))
server_sock.listen(1)

port = server_sock.getsockname()[1]
uuid = "714d9764-afd6-44ca-8913-de270903f1ab"
advertise_service( server_sock, "SampleServer",
                   service_id = uuid,
                   service_classes = [ uuid, SERIAL_PORT_CLASS ],
                   profiles = [ SERIAL_PORT_PROFILE ], 
                   )

print("Waiting for connection on RFCOMM channel %d" % port)
client_sock, client_info = server_sock.accept()
print("Accepted connection from ", client_info)

try:
    while True:
        data = client_sock.recv(1024)
        if len(data) == 0: break
        result=data.decode("utf-8")

        # if result=="space" :        
        keyboardEvents.PressKey(int(result));
        keyboardEvents.ReleaseKey(int(result));
        

        print("received [%s]" % result)
except IOError:
    pass

print("disconnected")
client_sock.close()
server_sock.close()
print("all done")