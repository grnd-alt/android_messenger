This is an android application created by Salim Belakkaf,
It is created using android Studio and Java.
The server behind the messaging is run via sockets on a raspberry pi.
On the same raspberry pi a mysql database is running,
which is communicating with the sockets server.

The database stores: Users with : ID, User_name, Password
					 unsend messanges: sender_ID, text, receiver_ID