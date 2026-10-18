use crate::AppState;
use axum::extract::State;
use axum::routing::get;
use axum::{Json, Router};
use serde_json::{Value, json};

async fn get_messages(State(messages): State<AppState>) -> Json<Value> {
    let read_lock = messages.read().await;
    Json(json!({"messages": &*read_lock}))
}

pub(crate) fn api(messages: AppState) -> Router {
    Router::new()
        .route("/messages", get(get_messages))
        .with_state(messages)
}
