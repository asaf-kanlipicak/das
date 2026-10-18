use crate::http::listener::start as start_http;
use crate::udp::listener::start as start_udp;
use std::sync::Arc;
use tokio::sync::RwLock;

pub(crate) mod http;
pub(crate) mod udp;

pub(crate) type AppState = Arc<RwLock<Vec<String>>>;

pub async fn start_listeners() {
    let messages: AppState = Arc::new(RwLock::new(Vec::new()));
    let http_task = tokio::spawn(start_http(messages.clone()));
    let udp_task = tokio::spawn(start_udp(messages));
    tokio::try_join!(http_task, udp_task).unwrap();
}
