# 🌈 C64 Plasma Effect Demo (Jetpack Compose)

> 🔥 Egy nosztalgikus Commodore 64 inspirálta plasma effekt, modern Android + Jetpack Compose környezetben újragondolva.

---

## ✨ Preview

<p align="center">
  <img src="https://raw.githubusercontent.com/placeholder/plasma-preview.gif" alt="C64 Plasma Effect Demo" width="400"/>
</p>
https://youtube.com/shorts/nw36Sm4k5VI

---

## 🧠 Mi ez az egész?

Ez a projekt egy **klasszikus retro “plasma effect” shader-szerű animáció**, amit:

- Kotlin + Jetpack Compose renderel
- CPU-n generál pixel bufferből
- C64-es színpalettát használ
- ditheringgel “retro monitor” hangulatot ad
- 60 FPS-szerű animációt futtat coroutine-ban

---

## 🚀 Features

✨ Valós idejű plasma animáció  
🎨 Commodore 64 inspirált 16 színű palette  
📺 Pixel-alapú render (Bitmap buffer)  
🌈 Sinus + distance field alapú mozgás  
🧩 Bayer dithering a retro hatásért  
⚡ Jetpack Compose UI integráció  
📱 Android native performance demo

---

## 🧱 Tech Stack

- Kotlin 🟣
- Jetpack Compose 🎨
- Android Canvas / Bitmap API 🧩
- Coroutines ⚡
- Kotlin Math (sin, sqrt) 📐

---

## 🧬 Hogyan működik?

A plasma effekt több matematikai hullám kombinációjából áll:

- `sin(x)`
- `sin(y)`
- `sin(x + y)`
- `sin(distance)`

Ezek összeadódnak, majd:

1. normalizálás történik
2. 0–15 indexre skálázás
3. C64 palette mapping
4. Bayer dithering finomítja a színátmenetet

---

## 🧪 Fő logika

```kotlin
val v = sin(x * f1 + time) +
        sin(y * f2 + time) +
        sin((x + y) * f3 + time) +
        sin(dist * f4 + time)
