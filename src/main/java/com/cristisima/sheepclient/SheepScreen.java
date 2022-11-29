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
    public static String CAT_EYES_BASE="Cat Eyes: ";
    public static String XRAY_BASE="xRay: ";
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
        int x_base=0;
        int width;

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
        x_base=-20;

        width=50;
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+x_base, this.height / 6 - 12 + 24 * (i >> 1), width, 20,
                Text.literal(OnOffName(FLY_BASE, Variables.FlyActive)), (button) -> {
            Variables.FlyActive=!Variables.FlyActive;
            button.setMessage(Text.literal(OnOffName(FLY_BASE, Variables.FlyActive)));
        }));
        x_base+=width+5;

        width=95;
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+x_base, this.height / 6 - 12 + 24 * (i >> 1), width, 20,
                Text.literal(OnOffName(NO_FALL_BASE, Variables.NoFall)), (button) -> {
            Variables.NoFall=!Variables.NoFall;
            button.setMessage(Text.literal(OnOffName(NO_FALL_BASE, Variables.NoFall)));
        }));
        x_base+=width+5;


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
        x_base=-20;

        width=120;
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+x_base, this.height / 6 - 12 + 24 * (i >> 1), width, 20,
                Text.literal(OnOffName(NO_POSITION_PACKET_BASE, Variables.noPositionPacket)), (button) -> {
            Variables.noPositionPacket=!Variables.noPositionPacket;
            button.setMessage(Text.literal(OnOffName(NO_POSITION_PACKET_BASE, Variables.noPositionPacket)));
        }));
        x_base+=width+5;

        width=40;
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+x_base, this.height / 6 - 12 + 24 * (i >> 1), width, 20,
                Text.literal("Fix Pos"), (button) -> {
            Variables.fixPositionActive=true;
        }));
        x_base+=width+5;


        i++;

        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)-50, this.height / 6 - 12 + 24 * (i >> 1), 150*3/4, 20,
                Text.literal(OnOffName(CAT_EYES_BASE, Variables.CatEyes.active)), (button) -> {
            Variables.CatEyes.active=!Variables.CatEyes.active;

            button.setMessage(Text.literal(OnOffName(CAT_EYES_BASE, Variables.CatEyes.active)));
        }));

        ButtonWidget CatEyesWidget=new ButtonWidget(this.width / 2 + calcXOffset(i)+150*3/4-50+25+5, this.height / 6 - 12 + 24 * (i >> 1), 20, 20,
                Text.literal(String.valueOf(Variables.CatEyes.forced_min)), (button) -> {
            Variables.CatEyes.forced_min=10;
        });
        this.addDrawableChild(CatEyesWidget);

        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+150*3/4-50+5, this.height / 6 - 12 + 24 * (i >> 1), 20, 20,
                Text.literal("-"), (button) -> {
            Variables.CatEyes.forced_min =Math.max(
                    0,
                    Variables.CatEyes.forced_min-1
            );

            CatEyesWidget.setMessage(Text.literal(String.valueOf(Variables.CatEyes.forced_min)));

        }));
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+150*3/4-50+50+5, this.height / 6 - 12 + 24 * (i >> 1), 20, 20,
                Text.literal("+"), (button) -> {
            Variables.CatEyes.forced_min =Math.min(
                    Variables.CatEyes.MAX,
                    Variables.CatEyes.forced_min+1
            );

            CatEyesWidget.setMessage(Text.literal(String.valueOf(Variables.CatEyes.forced_min)));
        }));

        i++;
        x_base=-20;

        width=60;
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+x_base, this.height / 6 - 12 + 24 * (i >> 1), width, 20,
                Text.literal(OnOffName(ALLOW_DEMO_BASE, !Variables.noDemo)), (button) -> {
            Variables.noDemo=!Variables.noDemo;
            button.setMessage(Text.literal(OnOffName(ALLOW_DEMO_BASE, !Variables.noDemo)));
        }));
        x_base+=width+5;

        width=80;
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+x_base, this.height / 6 - 12 + 24 * (i >> 1), width, 20,
                Text.literal(OnOffName(ALLOW_CREATIVE_BASE, !Variables.noCreative)), (button) -> {
            Variables.noCreative=!Variables.noCreative;
            button.setMessage(Text.literal(OnOffName(ALLOW_CREATIVE_BASE, !Variables.noCreative)));
        }));
        x_base+=width+5;


        i++;

        i++;
        x_base=-20;

        width=60;
        this.addDrawableChild(new ButtonWidget(this.width / 2 + calcXOffset(i)+x_base, this.height / 6 - 12 + 24 * (i >> 1), width, 20,
                Text.literal(OnOffName(XRAY_BASE, Variables.xRay.active)), (button) -> {
            Variables.xRay.active=!Variables.xRay.active;
            button.setMessage(Text.literal(OnOffName(XRAY_BASE, Variables.xRay.active)));
        }));
        x_base+=width+5;

    }


    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 15, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }
}
