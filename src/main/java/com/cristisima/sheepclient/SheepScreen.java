package com.cristisima.sheepclient;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.concurrent.atomic.AtomicBoolean;

public class SheepScreen extends Screen {
    private final Screen parent;

    public SheepScreen(Screen parent)
    {
        super(Text.literal("Sheep Client"));
        this.parent = parent;
    }

    public static String POSITION_PRECISION_BASE="Position Precision: ";
    public static String FLY_BASE="Fly: ";
    public static String NO_FALL_BASE="No Fall Dmg: ";
    public static String NO_POSITION_PACKET_BASE="No Position packet: ";
    public static String INITIAL_Y_OFFSET_BASE="Initial Y Offset: ";
    public static String ALLOW_DEMO_BASE="Demo?: ";
    public static String ALLOW_CREATIVE_BASE="Creative?: ";

    int injectedYVelocityVal=0;

    public static String OnOffName(String name, boolean state)
    {
        if(state)
            return name+"ON";
        else
            return name+"OFF";
    }

    public static int calcXOffset(int i)
    {
        if(i%2==1)
            return 5;
        else
            return -150-5;
    }

    @Override
    protected void init() {
        int i=-1;

        i++;
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)-50, this.height / 6 - 12 + 24 * (i >> 1), 150*3/4, 20,
                Text.literal(OnOffName(POSITION_PRECISION_BASE, Variables.PositionPrecision.active)), (button) -> {
            Variables.PositionPrecision.active=!Variables.PositionPrecision.active;
            button.setMessage(Text.literal(OnOffName(POSITION_PRECISION_BASE, Variables.PositionPrecision.active)));
        }));

        ButtonWidget positionPrecisionValue=new ButtonWidget(this.width / 2 + calcXOffset(i)+150*3/4-50+25+5, this.height / 6 - 12 + 24 * (i >> 1), 20, 20,
                Text.literal(""+Variables.PositionPrecision.PRECISION), (button) -> {

            SheepClient.LOGGER.info("precision reset");
            Variables.PositionPrecision.PRECISION = Variables.PositionPrecision.DEFAULT;

            button.setMessage(Text.literal(""+Variables.PositionPrecision.PRECISION));
        });
        this.addDrawableChild(positionPrecisionValue);

        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+150*3/4-50+5, this.height / 6 - 12 + 24 * (i >> 1), 20, 20,
                Text.literal("-"), (button) -> {
            Variables.PositionPrecision.PRECISION =Math.max(
                    0,
                    Variables.PositionPrecision.PRECISION-1
            );

            positionPrecisionValue.setMessage(Text.literal(""+Variables.PositionPrecision.PRECISION));

        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+150*3/4-50+50+5, this.height / 6 - 12 + 24 * (i >> 1), 20, 20,
                Text.literal("+"), (button) -> {
            Variables.PositionPrecision.PRECISION =Math.min(
                    5,
                    Variables.PositionPrecision.PRECISION+1
            );

            positionPrecisionValue.setMessage(Text.literal(""+Variables.PositionPrecision.PRECISION));
        }));

        i++;

        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i), this.height / 6 - 12 + 24 * (i >> 1), 50, 20,
                Text.literal(OnOffName(FLY_BASE, Variables.FlyActive)), (button) -> {
            Variables.FlyActive=!Variables.FlyActive;
            button.setMessage(Text.literal(OnOffName(FLY_BASE, Variables.FlyActive)));
        }));

        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+55, this.height / 6 - 12 + 24 * (i >> 1), 95, 20,
                Text.literal(OnOffName(NO_FALL_BASE, Variables.NoFall)), (button) -> {
            Variables.NoFall=!Variables.NoFall;
            button.setMessage(Text.literal(OnOffName(NO_FALL_BASE, Variables.NoFall)));
        }));


        i++;
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)-50, this.height / 6 - 12 + 24 * (i >> 1), 150*3/4, 20,
                Text.literal(OnOffName(INITIAL_Y_OFFSET_BASE, Variables.InitialHeightOffset.active)), (button) -> {
            Variables.InitialHeightOffset.active=!Variables.InitialHeightOffset.active;
            button.setMessage(Text.literal(OnOffName(INITIAL_Y_OFFSET_BASE, Variables.InitialHeightOffset.active)));
        }));

        ButtonWidget initialHeightOffset=new ButtonWidget(this.width / 2 + calcXOffset(i)+150*3/4-50+25+5, this.height / 6 - 12 + 24 * (i >> 1), 20, 20,
                Text.literal(""+Variables.InitialHeightOffset.offset), (button) -> {

            SheepClient.LOGGER.info("precision reset");
            Variables.InitialHeightOffset.offset = Variables.InitialHeightOffset.DEFAULT;

            button.setMessage(Text.literal(""+Variables.InitialHeightOffset.offset));
        });
        this.addDrawableChild(initialHeightOffset);

        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+150*3/4-50+5, this.height / 6 - 12 + 24 * (i >> 1), 20, 20,
                Text.literal("-"), (button) -> {
            Variables.InitialHeightOffset.offset =Math.max(
                    0,
                    Variables.InitialHeightOffset.offset-1
            );

            initialHeightOffset.setMessage(Text.literal(""+Variables.InitialHeightOffset.offset));

        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+150*3/4-50+50+5, this.height / 6 - 12 + 24 * (i >> 1), 20, 20,
                Text.literal("+"), (button) -> {
            Variables.InitialHeightOffset.offset =Math.min(
                    5,
                    Variables.InitialHeightOffset.offset+1
            );

            initialHeightOffset.setMessage(Text.literal(""+Variables.InitialHeightOffset.offset));
        }));


        i++;

        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i), this.height / 6 - 12 + 24 * (i >> 1), 120, 20,
                Text.literal(OnOffName(NO_POSITION_PACKET_BASE, Variables.noPositionPacket)), (button) -> {
            Variables.noPositionPacket=!Variables.noPositionPacket;
            button.setMessage(Text.literal(OnOffName(NO_POSITION_PACKET_BASE, Variables.noPositionPacket)));
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+125, this.height / 6 - 12 + 24 * (i >> 1), 40, 20,
                Text.literal("Fix Pos"), (button) -> {
            Variables.fixPositionActive=true;
        }));


        i++;

//        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)-50, this.height / 6 - 12 + 24 * (i >> 1), 150*3/4, 20,
//                Text.literal(INJECT_Y_VELOCITY), (button) -> {
//            Variables.injectedYVelocity=injectedYVelocityVal;
//        }));
//
//        ButtonWidget injectedYVelocity=new ButtonWidget(this.width / 2 + calcXOffset(i)+150*3/4-50+25+5, this.height / 6 - 12 + 24 * (i >> 1), 20, 20,
//                Text.literal(""+injectedYVelocityVal), (button) -> {
//
//            SheepClient.LOGGER.info("precision reset");
//            injectedYVelocityVal = 0;
//
//            button.setMessage(Text.literal(""+injectedYVelocityVal));
//        });
//        this.addDrawableChild(injectedYVelocity);
//
//        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+150*3/4-50+5, this.height / 6 - 12 + 24 * (i >> 1), 20, 20,
//                Text.literal("-"), (button) -> {
//            injectedYVelocityVal =Math.max(
//                    0,
//                    injectedYVelocityVal-1
//            );
//
//            injectedYVelocity.setMessage(Text.literal(""+injectedYVelocityVal));
//
//        }));
//        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+150*3/4-50+50+5, this.height / 6 - 12 + 24 * (i >> 1), 20, 20,
//                Text.literal("+"), (button) -> {
//            injectedYVelocityVal =Math.min(
//                    10,
//                    injectedYVelocityVal+1
//            );
//
//            injectedYVelocity.setMessage(Text.literal(""+injectedYVelocityVal));
//        }));
        i++;
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i), this.height / 6 - 12 + 24 * (i >> 1), 60, 20,
                Text.literal(OnOffName(ALLOW_DEMO_BASE, !Variables.noDemo)), (button) -> {
            Variables.noDemo=!Variables.noDemo;
            button.setMessage(Text.literal(OnOffName(ALLOW_DEMO_BASE, !Variables.noDemo)));
        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+65, this.height / 6 - 12 + 24 * (i >> 1), 80, 20,
                Text.literal(OnOffName(ALLOW_CREATIVE_BASE, !Variables.noCreative)), (button) -> {
            Variables.noCreative=!Variables.noCreative;
            button.setMessage(Text.literal(OnOffName(ALLOW_CREATIVE_BASE, !Variables.noCreative)));
        }));
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
