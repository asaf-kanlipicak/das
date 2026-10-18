use std::sync::{RwLock, Arc};
use tokio::net::{UdpSocket, TcpListener};
use axum::{routing::get, Router, response::Json, extract::State};
use serde_json::{Value, json};

type AppState = Arc<RwLock<Vec<String>>>;

#[tokio::main]
async fn main() {
    let udp_socket = UdpSocket::bind("0.0.0.0:7777").await.unwrap();
    let mut udp_buffer = [0; 1024];
    let messages: AppState = Arc::new(RwLock::new(Vec::new()));

    let app = Router::new()
        .route("/messages",get(get_messages))
        .with_state(messages.clone());

    let tcp_socket = TcpListener::bind("0.0.0.0:8000").await.unwrap();

    let udp_task = tokio::spawn(async move {
        loop{
            let (length, address) = udp_socket.recv_from(&mut udp_buffer).await.unwrap();
            let message = String::from_utf8(udp_buffer[..length].to_vec()).unwrap();
            messages.write().unwrap().push(message);
            udp_socket.send_to(b"ACK", address).await.unwrap();
        }
    }
    );

    let http_task = tokio::spawn(async move {
        axum::serve(tcp_socket, app).await.unwrap();
    });

    tokio::try_join!(udp_task, http_task).unwrap();

}

async fn get_messages(State(messages): State<AppState>) -> Json<Value>{
    let immutable_borrow = messages.read().unwrap();
    Json(json!({"messages": immutable_borrow.clone()}))
}