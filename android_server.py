import socket
import threading
import mysql.connector
import time
def get_cursor():
	global cursor,db
	db = mysql.connector.connect(
		host = "localhost",
		user = 'root',
		passwd = 'Minecraft12',
		database = 'messenger',
		)
	cursor = db.cursor()
def new_user(name,password):
	print(name,password)
	global cursor,db
	get_cursor()
	cursor.execute('INSERT into USER(User_name,Passwort)values(\"'+name+'\",\"'+password+'\");')
	for x in cursor:
		if x[0] == '':
			pass
	db.commit()
	cursor.close()
	db.close()	
	get_cursor()
	cursor.execute('select max(User_ID) from USER;')
	for x in cursor:
		user_id = x[0]
	return user_id
def find_user(ID):
	global cursor,db
	User_name = ''
	get_cursor()
	cursor.execute('SELECT * from USER where User_ID=\"'+ID+'\";')
	for x in cursor:
		User_name = x[1]
	db.commit()
	cursor.close()
	db.close()
	return User_name
def check_login(ID,user,password):
	global cursor,db
	get_cursor()
	cursor.execute("SELECT Passwort from USER where User_ID = "+ID+ ' and User_name = \"'+user+'\";')
	for x in cursor:
		dbpass = x[0]
	if dbpass == password:
		return True
def new_message(from_id,to_id,message):
	global cursor,db
	get_cursor()
	cursor.execute('INSERT into OPEN_MESSANGES(sender_ID,receiver_ID,content) values('+from_id+','+to_id+',\"'+str(message)+'\");')
	for x in cursor:
		if x[0] == '':
			pass
	db.commit()
	cursor.close
	db.close()
def get_message(to_id):
	print(to_id)
	global cursor,db
	get_cursor()
	cursor.execute("select * from OPEN_MESSANGES where receiver_ID = "+to_id+';')
	for x in cursor:
		print(x)
socket = socket.socket()
socket.bind(("192.168.1.101",8700))
socket.listen()
def seperator(string):
	counter = 0
	solved = []
	for x,i in enumerate(string):
		if i == ';':
			if string[counter:x] != '':
				solved.append(string[counter:x])
			counter = x+1
	return solved
def connection(client,addr):
	recv = seperator(client.recv(1024).decode()[2:])
	print(recv)
	if recv[0] == "message":
		print(recv)
		send_to = recv[1]
		content = recv[2]
		from_id = recv[3]
		print('sending '+content+' to '+send_to+' from: '+ from_id)
		new_message(from_id,send_to,content)
	elif recv[0] == "message request":
		get_message(recv[1])
	elif recv[0] == "register":
		print('recv')
		print("new user: ",recv[1])
		print("doing stuff")
		user_id = new_user(recv[1],recv[2])
		to_send = str(user_id) + "\n"
		client.send(to_send.encode())
	elif recv[0] == 'search':
		print("showing all users with the ID: "+recv[1])
		found = find_user(recv[1])+'\n'
		client.send(found.encode())
		print("was sended")
	elif recv[0] =='login':
		print("login from" ,recv)
		if check_login(recv[1],recv[2],recv[3]) == True:
			client.send("worked\n".encode())
			print("logged him in")
while True:
	(client,adress) = socket.accept()
	print('connected to: ',adress)
	newcon = threading.Thread(target = connection,args = (client,adress,))
	newcon.start()