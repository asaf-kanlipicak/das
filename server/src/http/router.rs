use crate::AppState;
use axum::extract::State;
use axum::http::Method;
use axum::routing::get;
use axum::{Json, Router};
use serde::Serialize;
use serde_json::{Value, json};
use tower_http::cors::{AllowOrigin, CorsLayer};

#[derive(Serialize)]
struct MessageRecord {
    index: usize,
    content: String,
}

async fn get_messages(State(messages): State<AppState>) -> Json<Value> {
    let read_lock = messages.read().await;
    let mut records: Vec<MessageRecord> = Vec::new();
    for (index, value) in read_lock.clone().iter().enumerate() {
        records.push(MessageRecord {
            index,
            content: value.clone(),
        })
    }
    Json(json!({"messages": records}))
}

pub(crate) fn api(messages: AppState) -> Router {
    let cors = CorsLayer::new()
        .allow_origin(AllowOrigin::list("http://localhost:5173".parse()))
        .allow_methods([Method::GET, Method::POST])
        .allow_headers([
            axum::http::header::AUTHORIZATION,
            axum::http::header::CONTENT_TYPE,
        ])
        .allow_credentials(true);

    Router::new()
        .route("/messages", get(get_messages))
        .with_state(messages)
        .layer(cors)
}
