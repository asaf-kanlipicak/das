use server::start_listeners;
#[tokio::main]
async fn main() {
    start_listeners().await
}
