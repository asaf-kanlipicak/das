use tokio::net::UdpSocket;

#[tokio::main]
async fn main() {
    let sock = UdpSocket::bind("0.0.0.0:7777").await.unwrap();
    let mut buf = [0; 1024];
    loop {
        let (len, addr) = sock.recv_from(&mut buf).await.unwrap();
        println!("{:?} bytes received from {:?}", len, addr);
        sock.send_to(&buf[..len], addr).await.unwrap();
    }
}
