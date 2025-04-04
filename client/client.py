import socket
import sys
import os

if len(sys.argv) != 3:
    print("Usage: python client.py <server_ip> <port>")
    sys.exit(1)

server_ip = sys.argv[1]
server_port = int(sys.argv[2])

try:
    print(f"Connected to server at {server_ip}:{server_port}")
    while True:
        # Create a TCP socket
        client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        client_socket.connect((server_ip, server_port))

        message = input("> ")  # Interactive input
        if not message:
            continue  # Ignore empty input

        if message.upper() == "CLS":
            os.system("cls" if os.name == "nt" else "clear")  # Cross-platform solution
            continue

        client_socket.sendall((message + "\n").encode())   # Send data to server
        response = client_socket.recv(1024).decode()  # Receive response

        if not response:
            print("Server disconnected.")
            break

        print("Response:", response)  # Print server response

except KeyboardInterrupt:
    print("\nClient exiting...")
except Exception as e:
    print(f"Error: {e}")
finally:
    client_socket.close()
