import controlP5.*; //<>// //<>//

String assets_folder = "assets/";
PImage current_screen, im_screen_home, im_screen0_0, im_screen0_1, im_screen1, im_screen2;
PFont f;
ControlP5 cp5;

static int tab_height = 70, tab_width = 120; 

// There are 3 tabs
int color_tb_pres = color(200, 200, 255);
int color_tb_fore = color(200, 255, 200); 
int color_tb_back = color(255, 200, 200);

int color_white = color(255, 255, 255);

int gr_x = 0, gr_y = 520;
int tp_x = 120, tp_y = 520;
int sg_x = 240, sg_y = 520;

int home_button_x = 14, home_button_y = 36, home_button_size = 48; 

// 0: Grocery, 1: Temperature, 2: Suggestions
int tab_selected = 0;

// Button images
PImage[] btn_dec_imgs = {null, null, null};
PImage[] btn_home_visible_imgs = {null, null, null};
PImage[] btn_home_invisible_imgs = {null, null, null};
PImage[] btn_inc_imgs = {null, null, null};
PImage[] btn_grocery_imgs = {null, null, null};
PImage[] btn_temp_imgs = {null, null, null};
PImage[] btn_sugg_imgs = {null, null, null};
PImage[] btn_alert_imgs = {null, null, null};
//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Grocery variables                                                                                    //  
//////////////////////////////////////////////////////////////////////////////////////////////////////////
Group groceryGroup;
// There are 2 options for grocery, 2 possible status
// true: Weight,  false: Date
boolean grocery_sort = true;
int tg_x = 130, tg_y = 473, tg_width = 100, tg_height = 17;
int color_on = color(0, 150, 136);
int color_off = color(77, 182, 172);
//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Temperature variables                                                                                //
//////////////////////////////////////////////////////////////////////////////////////////////////////////
Group temperatureGroup;
// A temperature controller has 2 buttons and 1 display
// There are a main controller and 3 smaller ones
final int BTN_TMP_MAIN_SIZE = 32, BTN_TMP_SMALL_SIZE = 32;
String temp_buttons[] = {
  "DecreaseMain", "IncreaseMain", 
  "DecreaseSec1", "IncreaseSec1", 
  "DecreaseSec2", "IncreaseSec2", 
  "DecreaseSec3", "IncreaseSec3",
  "DecreaseSec4", "IncreaseSec4"
};
float temp_buttons_pos[][] = {
  {206, 126}, {307, 126}, 
  {206, 206}, {307, 206}, 
  {206, 286}, {307, 286}, 
  {206, 366}, {307, 366},
  {206, 446}, {307, 446},
};
int temp_buttons_size[] = {
  BTN_TMP_MAIN_SIZE, BTN_TMP_MAIN_SIZE, 
  BTN_TMP_SMALL_SIZE, BTN_TMP_SMALL_SIZE, 
  BTN_TMP_SMALL_SIZE, BTN_TMP_SMALL_SIZE, 
  BTN_TMP_SMALL_SIZE, BTN_TMP_SMALL_SIZE,
  BTN_TMP_SMALL_SIZE, BTN_TMP_SMALL_SIZE
};

final int LBL_TMP_MAIN_SIZE = 32, LBL_TMP_SMALL_SIZE = 32;
String temp_labels[] = {
  "TempMain", "TempSec1", "TempSec2", "TempSec3", "TempSec4"
};
float temp_labels_pos[][] = {
  {263, 126}, 
  {263, 206}, 
  {263, 286}, 
  {263, 366},
  {263, 446}
};
int temp_labels_size[] = {
  LBL_TMP_MAIN_SIZE, 
  LBL_TMP_SMALL_SIZE, 
  LBL_TMP_SMALL_SIZE, 
  LBL_TMP_SMALL_SIZE,
  LBL_TMP_SMALL_SIZE
};
Textlabel txtl_temps[] = {null, null, null, null, null};
int temp_values[] = {0, 2, 1, -1, 1};
ControlFont temp_font;
//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Suggestions variables                                                                                //  
//////////////////////////////////////////////////////////////////////////////////////////////////////////
String sugg_labels[] = {"Meal1", "Meal2", "Meal3"};
String sugg_titles[] = {"Tomato-Cheese Sauce", "Tomato Soup", "Tomato Salad"};
Accordion suggestionsGroup;
int sgl_x = 20, sgl_y = 105, sgl_width = 320, sgl_height = 250;
int sgl_bar_height = 50;
//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Home variables                                                                                       //  
//////////////////////////////////////////////////////////////////////////////////////////////////////////
Group homeGroup;
String home_temp_labels[] = {
  "TempMain"
};
float home_temp_labels_pos[][] = {
  {240, 315}
};
int home_temp_labels_size[] = {
  LBL_TMP_MAIN_SIZE
};
String home_temp_buttons[] = {
  "DecreaseMain", "IncreaseMain"
};
float home_temp_buttons_pos[][] = {
  {183, 315}, {284, 315} 
};
int home_temp_buttons_size[] = {
  BTN_TMP_SMALL_SIZE, BTN_TMP_SMALL_SIZE
};
Textlabel txtl_home_temps[] = {null};

// The third button is just a trick to avoid an error when removing the buttons
String home_alert_buttons[] = {
  "Expire", "Missing", "Extra"
};
float home_alert_buttons_pos[][] = {
  {288, 426}, {288, 477}, {0, 0}
};
int home_alert_buttons_size[] = {
  BTN_TMP_SMALL_SIZE, BTN_TMP_SMALL_SIZE, 0
};
boolean home_alert_values[] = {
  false, true, true
};
//////////////////////////////////////////////////////////////////////////////////////////////////////////
//                                                                                                      //  
//////////////////////////////////////////////////////////////////////////////////////////////////////////
void setup() {
  size(360, 640);
  noStroke();
  cp5 = new ControlP5(this);
  
  PFont pfont = createFont("", 1);
  temp_font = new ControlFont(pfont, 25);
  
  setTabsAndHomeButtons();
  
  im_screen_home = loadImage(assets_folder + "Home.png");
  im_screen0_0 = loadImage(assets_folder + "Grocery0.png");
  im_screen0_1 = loadImage(assets_folder + "Grocery1.png");
  im_screen1 = loadImage(assets_folder + "Temperature.png");
  im_screen2 = loadImage(assets_folder + "Suggestions.png");

  Home(-1);
}

void draw() {
  background(current_screen);
}

public void setTabsAndHomeButtons() {

  // Load images
  for(int i = 0; i < 3; i++) {
    btn_home_visible_imgs[i] = loadImage(assets_folder + "btn_home_visible"+String.valueOf(i)+".png");
    btn_home_invisible_imgs[i] = loadImage(assets_folder + "btn_home_invisible"+String.valueOf(i)+".png");
    btn_dec_imgs[i] = loadImage(assets_folder + "btn_dec"+String.valueOf(i)+".png");
    btn_inc_imgs[i] = loadImage(assets_folder + "btn_inc"+String.valueOf(i)+".png");
    btn_grocery_imgs[i] = loadImage(assets_folder + "btn_grocery"+String.valueOf(i)+".png");
    btn_temp_imgs[i] = loadImage(assets_folder + "btn_temp"+String.valueOf(i)+".png");
    btn_sugg_imgs[i] = loadImage(assets_folder + "btn_sugg"+String.valueOf(i)+".png");
    btn_alert_imgs[i] = loadImage(assets_folder + "btn_alert"+String.valueOf(i)+".png");
  }

  cp5.addButton("HomeButton")
    .setValue(-1)
    .setPosition(home_button_x, home_button_y)
    .setSize(home_button_size, home_button_size)
    ;
  
  // Create the buttons
  cp5.addButton("Grocery")
    .setValue(0)
    .setPosition(gr_x, gr_y)
    .setSize(tab_width, tab_height)
    .setImages(btn_grocery_imgs)
    ;

  cp5.addButton("Temperature")
    .setValue(1)
    .setPosition(tp_x, tp_y)
    .setSize(tab_width, tab_height)
    .setImages(btn_temp_imgs)
    ;

  cp5.addButton("Suggestions")
    .setValue(2)
    .setPosition(sg_x, sg_y)
    .setSize(tab_width, tab_height)
    .setImages(btn_sugg_imgs)
    ;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Grocery                                                                                              //  
//////////////////////////////////////////////////////////////////////////////////////////////////////////
public void displayGrocery() {
  groceryGroup = new Group(cp5, "GroceryGroup");

  // Controllers
  Toggle tgSort = new Toggle(cp5, "GrocerySort");
  tgSort
    .setPosition(tg_x, tg_y)
    .setSize(tg_width, tg_height)
    .setValue(grocery_sort)
    .setMode(ControlP5.SWITCH)
    .setColor(new CColor(color_on, color_off, color_on, color_off, color_on))
    .setLabel("")
    .setGroup(groceryGroup)
    ;
  groceryGroup.addDrawable(tgSort);
  showHomeButton();
}

void GrocerySort(boolean value) {
  grocery_sort = value;
  if (grocery_sort) {
    current_screen = im_screen0_0;
  } else {
    current_screen = im_screen0_1;
  }
  println("toggle grocery: " + String.valueOf(value));
}

public void Grocery(int value) {
  if (tab_selected == value) {
    return;
  }

  clearBody();

  tab_selected = value;

  if (grocery_sort) {
    current_screen = im_screen0_0;
  } else {
    current_screen = im_screen0_1;
  }

  displayGrocery();
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Temperature                                                                                          //
//////////////////////////////////////////////////////////////////////////////////////////////////////////
public void displayTemperature() {
  temperatureGroup = new Group(cp5, "TemperatureGroup");

  // Controllers
  for (int i = 0; i < temp_labels.length; i++) {
    Textlabel current = new Textlabel(cp5, temp_labels[i], 0, 0);
    current
      .setText(String.valueOf(temp_values[i]))
      .setPosition(temp_labels_pos[i])
      ;
    current.setFont(temp_font);
    current.setColorValue(0x00000000);
    temperatureGroup.addDrawable(current);
    txtl_temps[i] = current;

    Button currentDec = new Button(cp5, temp_buttons[i*2]);
    currentDec
      .setPosition(temp_buttons_pos[i*2])
      .setSize(temp_buttons_size[i*2], temp_buttons_size[i*2])
      .setGroup(temperatureGroup)
      .setImages(btn_dec_imgs)
      .addListener(new TemperatureControlListener(i, temp_values, txtl_temps, false));
    temperatureGroup.addDrawable(currentDec);
   
    Button currentInc = new Button(cp5, temp_buttons[i*2+1]);
    currentInc
      .setPosition(temp_buttons_pos[i*2+1])
      .setSize(temp_buttons_size[i*2+1], temp_buttons_size[i*2+1])
      .setGroup(temperatureGroup)
      .setImages(btn_inc_imgs)
      .addListener(new TemperatureControlListener(i, temp_values, txtl_temps, true));
    temperatureGroup.addDrawable(currentInc);    
  }
  showHomeButton();
}

class TemperatureControlListener implements ControlListener {
  int mindex;
  int[] mvalues;
  Textlabel mlabels[];
  boolean mincrease;
  final int TEMP_OFFSET = 2;
  public TemperatureControlListener(int index, int[] values, Textlabel[] labels, boolean increase) {
    mindex = index;
    mvalues = values;
    mlabels = labels;
    mincrease = increase;
  }

  public void controlEvent(ControlEvent theEvent) {     
    if (mindex == 0) { // increase or decrease all temperatures
      for (int i = 0; i < mvalues.length; i++) {
        if (mincrease) {
          mvalues[i]++;
        } else {
          mvalues[i]--;
        }
      }
    } else {
      if (mincrease && mvalues[mindex] < mvalues[0] + TEMP_OFFSET) {
        mvalues[mindex]++;
      } else if (!mincrease && mvalues[mindex] > mvalues[0] - TEMP_OFFSET) {
        mvalues[mindex]--;
      }
    }

    for (int i = 0; i < mlabels.length; i++) {
      mlabels[i].setText(String.valueOf(mvalues[i]));
    }
  }
}
public void Temperature(int value) {
  if (tab_selected == value) {
    return;
  }

  clearBody();

  tab_selected = value;
  current_screen = im_screen1;

  displayTemperature();
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Suggestions                                                                                          //  
//////////////////////////////////////////////////////////////////////////////////////////////////////////
public void displaySuggestions() {
  suggestionsGroup = new Accordion(cp5, "SuggestionsGroup")
    .setPosition(sgl_x, sgl_y)
    .setMinItemHeight(sgl_height)
    .setWidth(sgl_width)
    ;

  for (int i = 0; i < sugg_labels.length; i++) {
    Group meal = new Group(cp5, sugg_labels[i]);
    meal.setBarHeight(sgl_bar_height)
        .setColor(new CColor(color_on, color_off, color_on, color_off, color_on))        
        .setColorBackground(color_white)
        .setColorForeground(color_off)
        .setColorLabel(color_on)
        .setFont(temp_font)
        .setLabel(sugg_titles[i]);    
    Button current = new Button(cp5, sugg_labels[i] + "btn");
    PImage im = loadImage(assets_folder + sugg_labels[i] + ".png");
    current.setPosition(0, 0)
      .setImages(im, im, im)
      .setGroup(meal)
      ;
    meal.addDrawable(current);
    suggestionsGroup.addItem(meal);    
  }
  
  showHomeButton();
}
public void Suggestions(int value) {
  if (tab_selected == value) {
    return;
  }

  clearBody();

  tab_selected = value;
  current_screen = im_screen2;

  displaySuggestions();
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////
// Home                                                                                                 //  
//////////////////////////////////////////////////////////////////////////////////////////////////////////
public void displayHome() {
  homeGroup = new Group(cp5, "HomeGroup");
  
  // Controllers
  for (int i = 0; i < home_temp_labels.length; i++) {
    Textlabel current = new Textlabel(cp5, home_temp_labels[i], 0, 0);
    current
      .setText(String.valueOf(temp_values[i]))
      .setPosition(home_temp_labels_pos[i])
      ;
    current.setFont(temp_font);
    current.setColorValue(0x00000000);
    homeGroup.addDrawable(current);
    txtl_home_temps[i] = current;

    Button currentDec = new Button(cp5, home_temp_buttons[i*2]);
    currentDec
      .setPosition(home_temp_buttons_pos[i*2])
      .setSize(home_temp_buttons_size[i*2], home_temp_buttons_size[i*2])
      .setGroup(homeGroup)
      .setImages(btn_dec_imgs)
      .addListener(new HomeTemperatureControlListener(i, temp_values, txtl_home_temps, false));
    homeGroup.addDrawable(currentDec);
   
    Button currentInc = new Button(cp5, home_temp_buttons[i*2+1]);
    currentInc
      .setPosition(home_temp_buttons_pos[i*2+1])
      .setSize(home_temp_buttons_size[i*2+1], home_temp_buttons_size[i*2+1])
      .setGroup(homeGroup)
      .setImages(btn_inc_imgs)
      .addListener(new HomeTemperatureControlListener(i, temp_values, txtl_home_temps, true));
    homeGroup.addDrawable(currentInc);    
  }
  
 for(int i = 0; i < home_alert_buttons.length - 1; i++) {
    Button btn_alert_days = new Button(cp5, home_alert_buttons[i]);
    btn_alert_days
      .setPosition(home_alert_buttons_pos[i])
      .setSize(home_alert_buttons_size[i], home_alert_buttons_size[i])
      //.setGroup(homeGroup) // do not set into a group!!! cause error in deletion ERROR
      .setImages(btn_alert_imgs)
      .addListener(new HomeAlertControlListener(home_alert_values[i]));
    //homeGroup.addDrawable(btn_alert_days);
  }
  
  //Trick to avoid error when deleting the buttons
  Button btn_alert_days = new Button(cp5, home_alert_buttons[2] + "2");
  btn_alert_days
    .setPosition(home_alert_buttons_pos[2])
    .setSize(home_alert_buttons_size[2], home_alert_buttons_size[2])
    //.setGroup(homeGroup) // do not set into a group!!! cause error in deletion ERROR
    //.setImages(btn_alert_imgs)
    .addListener(new HomeAlertControlListener(home_alert_values[2]));
  
}
class HomeAlertControlListener implements ControlListener {
  boolean mtoggle;
  public HomeAlertControlListener(boolean toggle) {
    mtoggle = toggle;
  }

  public void controlEvent(ControlEvent theEvent) {     
    GrocerySort(mtoggle);
    Grocery(0);    
  }
}
class HomeTemperatureControlListener implements ControlListener {
  int mindex;
  int[] mvalues;
  Textlabel mlabels[];
  boolean mincrease;
  final int TEMP_OFFSET = 2;
  public HomeTemperatureControlListener(int index, int[] values, Textlabel[] labels, boolean increase) {
    mindex = index;
    mvalues = values;
    mlabels = labels;
    mincrease = increase;
  }

  public void controlEvent(ControlEvent theEvent) {     
    if (mindex == 0) { // increase or decrease all temperatures
      for (int i = 0; i < mvalues.length; i++) {
        if (mincrease) {
          mvalues[i]++;
        } else {
          mvalues[i]--;
        }
      }
    } else {
      if (mincrease && mvalues[mindex] < mvalues[0] + TEMP_OFFSET) {
        mvalues[mindex]++;
      } else if (!mincrease && mvalues[mindex] > mvalues[0] - TEMP_OFFSET) {
        mvalues[mindex]--;
      }
    }

    for (int i = 0; i < mlabels.length; i++) {
      mlabels[i].setText(String.valueOf(mvalues[i]));
    }
  }
}
public void Home(int value) {
  if (tab_selected == value) {
    return;
  }

  clearBody();

  current_screen = im_screen_home;
  tab_selected = value;
  
  displayHome();
  hideHomeButton();
}

public void clearBody() {
  cp5.remove("GroceryGroup");
  cp5.remove("TemperatureGroup");
  cp5.remove("SuggestionsGroup");
  cp5.remove("HomeGroup");
  removeAlerts();
}

public void removeAlerts() {
  for(int i = 0; i < home_alert_buttons.length - 1; i++) {
    cp5.remove(home_alert_buttons[i]);
  }
}

public void showHomeButton() {  
  cp5
    .getController("HomeButton")
    .setImages(btn_home_visible_imgs)
    ;
  println("showHomeButton");
}

public void hideHomeButton() {
  cp5
    .getController("HomeButton")
    .setImages(btn_home_invisible_imgs)
    ;  
  println("hideHomeButton");
}

public void HomeButton(int value) { 
  Home(value);  
  println("home pressed");
}