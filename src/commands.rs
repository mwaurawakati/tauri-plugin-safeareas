use tauri::{AppHandle, command, Runtime};

use crate::models::*;
use crate::Result;
use crate::SafeareasExt;

#[command]
pub(crate) async fn set_color<R: Runtime>(
    app: AppHandle<R>,
    payload: SetColorRequest,
) -> Result<()> {
    app.safeareas().set_color(payload)
}

#[command]
pub(crate) async fn set_top_bar_color<R: Runtime>(
    app: AppHandle<R>,
    payload: SetColorRequest,
) -> Result<()> {
    app.safeareas().set_top_bar_color(payload)
}

#[command]
pub(crate) async fn set_bottom_bar_color<R: Runtime>(
    app: AppHandle<R>,
    payload: SetColorRequest,
) -> Result<()> {
    app.safeareas().set_top_bar_color(payload)
}

#[command]
pub(crate) async fn enable_fullscreen_scrolling<R: Runtime>(
    app: AppHandle<R>,
    payload: SetColorRequest,
) -> Result<()> {
    app.safeareas().set_top_bar_color(payload)
}