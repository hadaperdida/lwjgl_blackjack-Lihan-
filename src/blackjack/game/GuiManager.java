package blackjack.game;

import org.lwjgl.glfw.GLFW;
import blackjack.engine.Engine;
import blackjack.engine.Window;
import blackjack.engine.scene.EntityLoader;
import blackjack.engine.scene.Scene;
import imgui.ImGui;
import imgui.flag.ImGuiCond;
import imgui.flag.ImGuiWindowFlags;

public class GuiManager {

    // State logic
    private GameState gameState;
    private boolean pKeyPressed = false;

    // All the objects the GUI needs to control
    private Engine gameEngine;
    private EntityLoader entityLoader;
    private Scene scene;

    /**
    //Creates a new GUI manager.
    @param gameEngine   // Game engine (stop the game)
    @param entityLoader // Loader (start the game)
    @param scene    // The scene (load entities)
     */
    public GuiManager(Engine gameEngine, EntityLoader entityLoader, Scene scene) {
        this.gameEngine = gameEngine;
        this.entityLoader = entityLoader;
        this.scene = scene;
        this.gameState = GameState.MAIN_MENU; // Set the initial state
    }

    
     // Return the current game state.
    public GameState getGameState() {
        return this.gameState;
    }

    // Handles keyboard input for the GUI
    public void handleInput(Window window) {
        if (gameState == GameState.PLAYING && window.isKeyPressed(GLFW.GLFW_KEY_P)) {
            if (!pKeyPressed) {
                gameState = GameState.PAUSED;
                pKeyPressed = true;
            }
        } else if (!window.isKeyPressed(GLFW.GLFW_KEY_P)) {
            pKeyPressed = false;
        }
    }

    public void drawGui() {
        ImGui.newFrame();

        switch (gameState) {
            case MAIN_MENU:
                drawMainMenuGUI();
                break;
            case CREDITS:
                drawCreditsGUI();
                break;
            case PAUSED:
                drawPauseGUI();
            case PLAYING:
                drawInGameGUI();
                break;
        }
        ImGui.endFrame();
        ImGui.render();
    }

    private void drawPauseGUI() {
        float viewPortX = ImGui.getIO().getDisplaySizeX();
        float viewPortY = ImGui.getIO().getDisplaySizeY();
        ImGui.setNextWindowSize(200, 150);
        ImGui.setNextWindowPos(viewPortX * 0.5f, viewPortY * 0.5f, ImGuiCond.Always, 0.5f, 0.5f);
        ImGui.begin("Paused(?)", ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);

        float buttonWidth = 150f;
        float buttonHeight = 30f;
        ImGui.setCursorPosX((ImGui.getWindowWidth() - buttonWidth) * 0.5f);
        
        if (ImGui.button("Resume", buttonWidth, buttonHeight)) {
            this.gameState = GameState.PLAYING;
        }

        ImGui.setCursorPosX((ImGui.getWindowWidth() - buttonWidth) * 0.5f);
        if (ImGui.button("Settings", buttonWidth, buttonHeight)) {
            // PlaceHolder for the future
        }

        ImGui.setCursorPosX((ImGui.getWindowWidth() - buttonWidth) * 0.5f);
        if (ImGui.button("Back to Menu", buttonWidth, buttonHeight)) {
            this.gameState = GameState.MAIN_MENU;
        }
        ImGui.end();
    }

    // Basic Main Menu GUI
    private void drawMainMenuGUI() {
        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
        ImGui.setNextWindowSize(ImGui.getIO().getDisplaySize());
        ImGui.begin("Menu", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);

        float windowWidth = ImGui.getWindowSizeX();
        float buttonWidth = 120f;
        float buttonHeight = 40f;
        ImGui.setCursorPosX((windowWidth - buttonWidth) * 0.5f);
        ImGui.setCursorPosY(ImGui.getWindowSizeY() * 0.4f);
        
        if (ImGui.button("Play", buttonWidth, buttonHeight)) {
            this.entityLoader.loadEntities(this.scene);
            this.gameState = GameState.PLAYING;
        }
        ImGui.setCursorPosX((windowWidth - buttonWidth) * 0.5f);
        if (ImGui.button("Settings", buttonWidth, buttonHeight)) {
            // PlaceHolder
        }
        ImGui.setCursorPosX((windowWidth - buttonWidth) * 0.5f);
        if (ImGui.button("Credits", buttonWidth, buttonHeight)) {
            this.gameState = GameState.CREDITS;
        }

        ImGui.setCursorPosX((windowWidth - buttonWidth) * 0.5f);
        if (ImGui.button("Quit", buttonWidth, buttonHeight)) {
            gameEngine.stop();
        }
        ImGui.end();
    }

    // Credits
    private void drawCreditsGUI() {
        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
        ImGui.setNextWindowSize(ImGui.getIO().getDisplaySize());
        ImGui.begin("Credits", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);

        ImGui.setCursorPosY(ImGui.getWindowSizeY() * 0.2f);

        drawCenteredText("Game Developed By:");
        ImGui.newLine();
        drawCenteredText("Lihan Martinez");
        drawCenteredText("Fernando Rojas");
        drawCenteredText("Nicolas Vega"); 
        drawCenteredText("Carlos Gernhofer");

        float buttonWidth = 100f;
        float buttonHeight = 30f;
        ImGui.setCursorPosX((ImGui.getWindowSizeX() - buttonWidth) * 0.5f);
        ImGui.setCursorPosY(ImGui.getWindowSizeY() - buttonHeight - 20f);
        if(ImGui.button("Back", buttonWidth, buttonHeight)) {
            this.gameState = GameState.MAIN_MENU;
        }
        ImGui.end();
    }

    // In-Game GUI
    private void drawInGameGUI() {
        ImGui.setNextWindowPos(10, 10, ImGuiCond.Once);
        ImGui.setNextWindowSize(250, 100, ImGuiCond.Once);
        ImGui.begin("Info papi");
        ImGui.text("SHIFT + Mouse to look around");
        ImGui.text("Move with WASD & UP/DOWN");
        
        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
        ImGui.showDemoWindow();

        ImGui.end(); 
    }
   
    // Helper function to keep the future menu buttons in the middle
    private void drawCenteredText(String text) {
        float windowWidth = ImGui.getWindowSizeX();
        float textWidth = ImGui.calcTextSize(text).x;
        ImGui.setCursorPosX((windowWidth - textWidth) * 0.5f);
        ImGui.text(text);
    }
}