use crate::AppState;
use crate::http::router::api;
use tokio::net::TcpListener;

pub(crate) async fn start(messages: AppState) {
    let tcp_socket = TcpListener::bind("0.0.0.0:8000")
        .await
        .expect("bind tcp: 8000");
    let router = api(messages);
    axum::serve(tcp_socket, router).await.expect("axum serve");
}
