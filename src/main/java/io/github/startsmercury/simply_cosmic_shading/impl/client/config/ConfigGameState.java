package io.github.startsmercury.simply_cosmic_shading.impl.client.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.ZoneLoader;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.ui.widgets.CRButton;
import io.github.startsmercury.simply_cosmic_shading.impl.client.SimplyCosmicShading;

public class ConfigGameState extends GameState {
    private final SimplyCosmicShading simplyCosmicShading;
    private final GameState previousState;
    private final String on;
    private final String off;

    private Button terrainBlockShadingEnabledButton;
    private Button blockItemShadingEnabledButton;

    public ConfigGameState(
        final SimplyCosmicShading simplyCosmicShading,
        final GameState previousState
    ) {
        this.simplyCosmicShading = simplyCosmicShading;
        this.previousState = previousState;
        this.on = Lang.get("on_state");
        this.off = Lang.get("off_state");
    }

    @Override
    public void create() {
        super.create();

        final var config = this.simplyCosmicShading.getConfig();

        this.terrainBlockShadingEnabledButton =
            this.newToggle("Terrain Block Shading: ", config.terrainBlockShadingEnabled());
        this.blockItemShadingEnabledButton =
            this.newToggle("Block Item Shading: ", config.blockItemShadingEnabled());

        final var doneButton = new CRButton(Lang.get("doneButton"));
        final var optionsTable = new Table();
        optionsTable.align(Align.center).defaults().pad(12.5F, 12f, 12.5f, 12f).size(250.0F, 50.0F);

        final var table = new Table();
        table.setFillParent(true);

        optionsTable.add(terrainBlockShadingEnabledButton, blockItemShadingEnabledButton);

        table.add(optionsTable);
        table.row();
        table.add(doneButton).expandX().size(250.0F, 50.0F);

        doneButton.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                ConfigGameState.this.returnToPrevious();
            }
        });

        this.stage.addActor(table);
        Gdx.input.setInputProcessor(this.stage);
    }

    private Button newToggle(final String name, final boolean checked) {
        final var button = new CRButton();

        if (checked) {
            button.setChecked(true);
            button.setText(name + on);
        } else {
            button.setText(name + off);
        }

        button.addListener(new ClickListener() {
            @Override
            public void clicked(final InputEvent event, final float x, final float y) {
                if (button.isChecked()) {
                    button.setText(name + on);
                } else {
                    button.setText(name + off);
                }
            }
        });

        return button;
    }

    private void returnToPrevious() {
        switchToGameState(this.previousState);
    }

    @Override
    public void switchAwayTo(final GameState gameState) {
        super.switchAwayTo(gameState);
        Gdx.input.setInputProcessor(null);

        final var oldConfig = this.simplyCosmicShading.getConfig();

        final var terrainBlockShadingEnabled = this.terrainBlockShadingEnabledButton.isChecked();
        final var blockItemShadingEnabled = this.blockItemShadingEnabledButton.isChecked();

        this.simplyCosmicShading.setConfig(new ConfigV0(
            terrainBlockShadingEnabled,
            blockItemShadingEnabled
        ));

        this.simplyCosmicShading.saveConfig();

        final var refreshingUnneeded =
            oldConfig.terrainBlockShadingEnabled() == terrainBlockShadingEnabled;

        if (refreshingUnneeded) {
            return;
        }

        // seems optional...just to be safe
        synchronized (ZoneLoader.worldGenLock) {
            final var world = InGame.getWorld();
            for (final var zone : world.getZones()) {
                for (final var region : zone.getRegions()) {
                    for (final var chunk : region.getChunks()) {
                        chunk.flagForRemeshing(false);
                    }
                }
            }
            GameSingletons.meshGenThread.requestImmediateResorting();
        }
    }

    @Override
    public void render() {
        super.render();
        this.stage.act();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.returnToPrevious();
        }

        ScreenUtils.clear(0.145F, 0.078F, 0.153F, 1.0F, true);
        Gdx.gl.glEnable(2929);
        Gdx.gl.glDepthFunc(513);
        Gdx.gl.glEnable(2884);
        Gdx.gl.glCullFace(1029);
        Gdx.gl.glEnable(3042);
        Gdx.gl.glBlendFunc(770, 771);
        Gdx.gl.glCullFace(1028);
        this.stage.draw();
        Gdx.gl.glEnable(2884);
        Gdx.gl.glCullFace(1029);
        Gdx.gl.glDepthFunc(519);
    }
}
