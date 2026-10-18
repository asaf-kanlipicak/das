use crate::AppState;
use tokio::net::UdpSocket;

pub(crate) async fn start(messages: AppState) {
    const MAXIMUM_PACKET_SIZE: usize = 508;
    let udp_socket = UdpSocket::bind("0.0.0.0:7777")
        .await
        .expect("bind udp: 7777");
    let mut udp_buffer = [0; 1024];
    loop {
        let (length, address) = udp_socket.recv_from(&mut udp_buffer).await.unwrap();
        if length <= MAXIMUM_PACKET_SIZE {
            let message = String::from_utf8(udp_buffer[..length].to_vec()).unwrap();
            messages.write().await.push(message);
            udp_socket.send_to(b"ACK", address).await.unwrap();
        }
    }
}
