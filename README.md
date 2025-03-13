# Project Makao

<p align="center">
  <img src="https://github.com/user-attachments/assets/dff985a0-b88c-4c68-8065-0139cc66833b" align="center" height="auto" width="75%">
</p>

## Introduction
**Makao** is a 2D card game developed in Java using the **LibGDX** framework.

### Key Features:
- **Game Mechanics:** Implements the rules of the traditional **Makao** card game, allowing **players** to engage in virtual matches.  
- **Graphics and Assets:** Contains an `assets` directory, which likely includes visual and audio resources essential for the game's interface and experience.  
- **Code Architecture:** Follows the **Model-View-Controller (MVC)** pattern to structure the code efficiently, ensuring better separation of concerns and maintainability.  

> *This project serves as a digital adaptation of the Makao card game, leveraging Java and LibGDX to offer an interactive gaming experience.*

## Technology Stack
- **Java**
- **Gradle**
- **Game Library:** LibGDX

## Installation
> [!IMPORTANT]
> This game was developed for 1080p. Game screen is not rensponsive so play on maximize screen.

### 1. Clone the Repository

Start by cloning the repository to your local machine:
```bash
git clone https://github.com/WilinskiW/Makao.git
cd Makao
```
#### Set Up the Project
Ensure you have Java 8 or later installed on your machine. If not, download and install it.
You will also need Gradle installed. If you don't have Gradle installed, follow the installation instructions on the Gradle website.

#### Build the Project
```bash
./gradlew build
```
This will compile the game and generate the necessary files for execution.

#### Run project
Run `DesktopLauncher.main()` located in `desktop/src/com/wil/makao/DesktopLauncher.java`

### 2. Install compile game
> [!NOTE]
> You don't need JVM install on your computer.

**Download zip:** [Google Drive - Makao.zip](https://drive.google.com/drive/u/2/folders/1Ad6CjdYH7H4gRes2nDK84m96wEBWDWEv)

---

## Game Rules

### Main Goal
The objective of the game is to **get rid of all your cards** before your opponents do.

### Setup
- Each player is dealt **five cards**.
- The remaining deck is placed **face down** as the draw pile.
- The **top card** from the draw pile is placed **face up** to start the discard pile.

### Gameplay
- Players take turns **in a clockwise direction**.
- On their turn, a player must **play a card** that matches the **rank or suit** of the top card in the discard pile.
- If a player **cannot play**, they must **draw one card** from the draw pile. If this card is playable, they can play it immediately; otherwise, their turn ends.

### Special Cards and Their Effects
- **2:** The next player must **draw two cards** or play another '2' to pass the penalty to the subsequent player.
- **3:** Similar to '2', but the penalty is **three cards**.
- **4:** **Skips** the next player's turn.
- **Jack (J):** The player can request a **specific rank** from the next player, who must then play a card of that rank or a **Jack** to change the requested rank.
- **Queen (Q):** Can be **played on any card**, and any card can be played on a Queen.
- **King of Hearts:** The next player must **draw five cards**.
- **King of Spades:** The previous player must **draw five cards**.
- **Ace (A):** Allows the player to **change the current suit**.
- **Joker:** Acts as a **wildcard**, substituting for any card or effect.

### Additional Rules
- **Makao Declaration:** When a human player has only **one card left**, they must **declare "Makao!"** before the next player’s turn. **Failing to do so results in a penalty of drawing one card.**
- **Stacking Penalties:** Penalties from **'2's and '3's** can be stacked. For example, if a player plays a '2', the next player can play another '2', increasing the penalty to **four cards** for the subsequent player.
--- 
