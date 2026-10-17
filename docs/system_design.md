# Design

The system design of DAS shapes around these two primary principles:

- minimizing the network overhead without compromising on security and integrity

- maximizing the utilization of the network availability without burdening the phone battery too much

This way, we aim to transfer data even when:

- the network is available for a brief moment

- or is constantly available but very weak thus causing timeouts on traditional methods.

## Protocol

DAS implements a custom protocol over UDP to skip the overhead of TCP handshake, TLS handshake and the bloat of HTTP. The design assumes there is no server-side connectivity issue at all times, and focuses on the client side network availability.

### Encryption

- The key exchange happens over HTTPS when the client first connects to the server, ideally with a stable connection. The key does not expire by default, only gets rotated on demand. Thus connections start with a secure channel already available.

- Master key of a symmetric encryption algorithm is available at both client and the server. Each message carries a nonce + encrypted data. Nonce can be derived from the master key.

### Synchronizations

- The client sends SYN whenever it has a message to send, or wants to receive the pending messages.

- The mobile phones' connections are behind NAT mappings that expire quickly, so the server can not send SYN directly. To avoid burdening the phone battery with client side polling, the server wakes the client up through a Firebase Cloud Messaging (FCM) trigger. The client then sends messages to the server.

- At all times, the server sends messages to the client's last known IP address and port number. This is only done right after the client has sent a message.

### Acknowledgements

- ACK is sent only after successfully persisting the message. 

- A message on queue is only marked sent after receiving ACK.

### Retries

- Messages are retried when the connection is known to be alive. 

- Retries happen with an exponential backoff with a cap.

- Messages carry a unique message id to ensure idempotency.

### Queues

- Client queues its messages when there is no connection.

- The server always queues its messages and sends them only at time intervals shortly after receiving a message from the client.

### Payloads

- The messages will be maximum 508 bytes to avoid fragmentation and minimize packet loss.

- 508 minus the total size of our own headers is the maximum size of data we can carry in a single message. Anything over it must be chunked at application layer.

### Concurrency

- To maximize the throughput without overwhelming the cellular modem, there is a limit of maximum packages that can be in flight or awaiting ACK at any time.

- This number is fixed, there is no dynamic congestion control.

- This limit can start with 5, and tuned according to need later.



## Mobile App

### Triggers

The mobile app does not remain always-on at background. It wakes up with triggers. These can be:

- FCM triggers, prompting the application to SYN and start a connection.

- Connection availability callbacks from the OS to start sending the queued messages.

- Location captures every N minutes.

- Location history pushes every K minutes.

- Weather data requests every M minutes.

The app should run as a foreground service with a persistent notification to be able to receive notifications and callbacks in the background.



## Web Client

- Web client is for platform agnostic access for users with a stable internet connection.

- Simply uses HTTPS.

- Aimed to be used by the people who are not in an extreme situation to provide support for people in extreme situation.



## iOS Client

- Currently not in roadmap and is not researched thorougly.

- Can later be added if the protocol is possible to be ported natively in iOS.


