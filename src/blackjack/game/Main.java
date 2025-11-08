package blackjack.game;

import org.lwjgl.glfw.GLFW;

import blackjack.engine.*;
import blackjack.engine.graph.Render;
import blackjack.engine.scene.Camera;
import blackjack.engine.scene.EntityLoader;
import blackjack.engine.scene.Scene;
import imgui.ImGui;
import imgui.ImGuiIO;

public class Main implements IAppLogic, IGuiInstance {

    // Fields
    private EntityLoader entityLoader;
    private Window window;
    private Scene scene;
    private static Engine gameEngine;

    // All the UI logic is handled by the GuiManager
    private GuiManager guiManager;

    public static void main(String[] args) {
        Main main = new Main();
        gameEngine = new Engine("Blackjack LWJGL", new Window.WindowOptions(), main);
        gameEngine.start();
    }

    @Override
    public void init(Window window, Scene scene, Render render) {
        this.window = window;
        this.scene = scene;
        this.entityLoader = new EntityLoader();
        
        // Create the GuiManager and pass it the objects it needs to control
        this.guiManager = new GuiManager(gameEngine, entityLoader, scene);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) {
        // Let the GUI manager handle its input
        guiManager.handleInput(window);

        // Stop if the GUI consumed the input
        if (inputConsumed) {
            return;
        }

        // Only run the 3D game input if the state is PLAYING
        if (guiManager.getGameState() != GameState.PLAYING) {
            return;
        }

        // Original movement (probably changed)
        float move = diffTimeMillis * Consts.MOVEMENT_SPEED;
        Camera camera = scene.getCamera();
        
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)) {
            camera.moveForward(move);
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_S)) {
            camera.moveBackwards(move);
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)) {
            camera.moveLeft(move);
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_D)) {
            camera.moveRight(move);
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_UP)) {
            camera.moveUp(move);
        } else if (window.isKeyPressed(GLFW.GLFW_KEY_DOWN)) {
            camera.moveDown(move);
        }

        MouseInput mouseInput = window.getMouseInput();
        
        if (window.isKeyPressed(GLFW.GLFW_KEY_LEFT_SHIFT)){
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(displVec.x * Consts.MOUSE_SENS),
                    (float) Math.toRadians(displVec.y * Consts.MOUSE_SENS));
        }
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        // Only update the game world if the state is PLAYING
        if (guiManager.getGameState() != GameState.PLAYING) {
            // rotation += 1.5;
            // if (rotation > 360){
            //     rotation = 0;
            // }
            // cubeEntity.setRotation(1, 1, 1, (float) Math.toRadians(rotation));
            // cubeEntity.updateModelMatrix();
            return;
        }
        
    }

    @Override
    public void drawGui() {
        // All drawing logic is handled by GuiManager
        guiManager.drawGui();
    }

    @Override
    public boolean handleGuiInput(Scene scene, Window window) {
        ImGuiIO imGuiIO = ImGui.getIO();
        MouseInput mouseInput = window.getMouseInput();
        Vector2f mousePos = mouseInput.getCurrentPos();

        imGuiIO.addMousePosEvent(mousePos.x, mousePos.y);
        imGuiIO.addMouseButtonEvent(0, mouseInput.isLeftButtonPressed());
        imGuiIO.addMouseButtonEvent(1, mouseInput.isRightButtonPressed());

        return imGuiIO.getWantCaptureMouse() || imGuiIO.getWantCaptureKeyboard();
    }

    @Override
    public void cleanup() {
        //TODO
    }
}