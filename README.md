# ChatGPTRetrofitGsonHilt
ChatGPTのStream APIをAndroidアプリで使う

## 使用技術
- Version Catalogを使ったマルチモジュール構成
  - Version Catalogの保管が効くようにGradleをKotlinにしました。
- Dagger Hiltを使ったDIの実装
- View Bindingを使ったViewの実装
- [ChatGPTのStreamモード](https://platform.openai.com/docs/api-reference/completions/create#completions/create-stream)を[okhttp-sse](https://github.com/square/okhttp/tree/master/okhttp-sse)を使って実装
- Jsonの変換にGsonを使用

## 動作している様子

<img src="https://user-images.githubusercontent.com/19218690/232063716-c9601c02-b3b4-47ab-b3f8-6e16bcc18ec4.gif" width="350" alt="動作している様子">
