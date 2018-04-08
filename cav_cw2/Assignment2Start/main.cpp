#include <stdio.h>
#include <stdlib.h>
#include <vector>
#include <cmath>

#include <GL/glut.h>
#define GLUT_KEY_ESCAPE 27
#ifndef GLUT_WHEEL_UP
#define GLUT_WHEEL_UP 3
#define GLUT_WHEEL_DOWN 4
#endif

#include "mat.h"
#include "vec.h"
#include "vol.h"
#include "trf.h"

#define WIDTH 128
#define HEIGHT 256

static cVolumeData* volumeData = NULL;
static unsigned char threshold = 75;
static float t1 = 7.f;
static float t2 = 14.f;
static float t3 = 0.f;
static std::vector<vec4> transferFunction(256);

void ComputeTransferFunction() {
  std::vector<cp> colorKnots = {
    cp(.91f, .7f, .61f, 0),
    cp(.91f, .7f, .61f, 80),
    cp(1.0f, 1.0f, .85f, 82),
    cp(1.0f, 1.0f, .85f, 256)
  };

  std::vector<cp> alphaKnots = {
    cp(0.0f, 0),
    cp(0.0f, 40),
    cp(0.2f, 60),
    cp(0.05f, 63),
    cp(0.0f, 80),
    cp(0.9f, 82),
    cp(1.0f, 256)
  };

  std::vector<cp> tempColorKnots = {
    cp(.91f, .7f, .61f, 0),
    cp(.91f, .7f, .61f, 80),
    cp(1.0f, 1.0f, .85f, 82),
    cp(1.0f, 1.0f, .85f, 256)
  };

  std::vector<cp> tempAlphaKnots = {
    cp(0.0f, 0),
    cp(0.0f, 40),
    cp(0.2f, 60),
    cp(0.05f, 63),
    cp(0.0f, 80),
    cp(0.9f, 82),
    cp(1.0f, 256)
  };

  std::vector<cubic> colorCubic = cubic::CalculateCubicSpline(
    colorKnots.size() - 1, tempColorKnots);
  std::vector<cubic> alphaCubic = cubic::CalculateCubicSpline(
    alphaKnots.size() - 1, tempAlphaKnots);

  int numTF = 0;
  for(std::size_t i = 0, max = colorKnots.size() - 1; i != max; ++i) {
    int steps = colorKnots.at(i + 1).isoValue - colorKnots.at(i).isoValue;
    for(int j = 0; j < steps; j++) {
      float k = (float)j / (float)(steps - 1);
      transferFunction.at(numTF++) = colorCubic.at(i).GetPointOnSpline(k);
    }
  }

  numTF = 0;
  for(std::size_t i = 0, max = alphaKnots.size() - 1; i != max; ++i) {
    int steps = alphaKnots.at(i + 1).isoValue - alphaKnots.at(i).isoValue;
    for(int j = 0; j < steps; j++) {
      float k = (float)j / (float)(steps - 1);
      transferFunction.at(numTF++).w = alphaCubic.at(i).GetPointOnSpline(k).w;
    }
  }

  for(int i = 0; i < 256; i++){
    transferFunction.at(i) = transferFunction.at(i) * 255.0f;
  }
}

vec3 linearTF(vec3 a, vec3 b, float t) {
  return a * (1-t) + b * t;
}

vec3 quadraticTF(vec3 a, vec3 b, float t) {
  return a * (1-t*t) + b * t*t;
}

vec3 skullTF1(vec3 skin, vec3 bone, float t){
  float a = .35f;
  float b = .60f;
  float c = .80f;

  if(t<a){
    return vec3::Zero();
  } else if(t>=a && t<b){
    return skin;
  } else if(t>=b && t<c){
    return skin * (-t) + bone * (t);
  } else{
    return bone;
  }  
}

vec3 skullTF(vec3 skin, vec3 bone, float t){
  float a = .35f;
  float b = .60f;
  float e = std::exp(-t);

  if(t<a){
    return vec3::Zero();
  } else if(t>=a && t<b){
    return skin * (1/(1+e));
  } else{
    return bone;
  }  
}

vec4 CleanColor(vec4 color) {
  float x = color.r();
  float y = color.g();
  float z = color.b();
  float w = color.a();

  if(x > 1.0f)
    x = 1.0f;
  if(x < 0.0f)
    x = 0.0f;

  if(y > 1.0f)
    y = 1.0f;
  if(y < 0.0f)
    y = 0.0f;

  if(z > 1.0f)
    z = 1.0f;
  if(z < 0.0f)
    z = 0.0f;

  if(w > 1.0f)
    w = 1.0f;
  if(w < 0.0f)
    w = 0.0f;

  return vec4(x, y, z, w);
}

void Update(void) { glutPostRedisplay(); }

void Draw(void) {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  glPointSize(2.0);
  glBegin(GL_POINTS);

  int depth = volumeData->GetDepth();
  int height = volumeData->GetHeight();
  int width = volumeData->GetWidth();
  
  for (int z = 0; z < depth; z++) {
    for (int y = 0; y < height; y++) {
      float alpha_acc = 0.0f;
      vec3 skin = vec3(255, 255, 0) / 128.0f;
      vec3 bone = vec3(0, 0, 255) / 128.0f;
      vec3 color = vec3::Zero();
      for (int x = 0; x < width; x++) {
        unsigned char val = volumeData->Get(x, y, z);

        float alpha_cur = val / 255.0;
        alpha_acc = alpha_cur + (1 - alpha_cur) * alpha_acc;

        float intensity = .0f;
        if (x > 0) {
          unsigned char prev_val = volumeData->Get(x, y-1, z);
          intensity = ((1 - alpha_acc) * prev_val);
        }

        float intensityf = intensity / 255.0f;
        if (intensity < t1){
          continue;
        } else if (intensity >= t1 && intensity < t2) {
          color = color + (skin * intensityf);
        } else {
          color = color + (bone * intensityf);
        }
        
        // original
        // if(val > threshold) {
        //   vec3 color = vec3(val, val, val) / 255.0f;
        //   glColor3f(color.r(), color.g(), color.b());
        //   glVertex3f(y, z, 0);
        //   break;
        // }
      }
      glColor3f(color.r(), color.g(), color.b());
      glVertex3f(y, z, 0);
    }
  }

  glEnd();

  glFlush();
  glutSwapBuffers();
}

void KeyEvent(unsigned char key, int x, int y) {
  switch (key) {
    case GLUT_KEY_ESCAPE:
      exit(EXIT_SUCCESS);
      break;
    case GLUT_KEY_UP:
      threshold = (threshold == 255) ? 255 : threshold + 10;
      break;
    case GLUT_KEY_DOWN:
      threshold = (threshold == 0) ? 0 : threshold - 10;
      break;
    case GLUT_KEY_F1:
      t1 = (t1 == 255) ? 255 : t1 + 1;
      printf("t1: %.2f, t2: %.2f, t3: %.2f\n", t1, t2, t3);
      break;
    case GLUT_KEY_F2:
      t1 = (t1 == 0.0) ? 0.0 : t1 - 1;
      printf("t1: %.2f, t2: %.2f, t3: %.2f\n", t1, t2, t3);
      break;
    case GLUT_KEY_F3:
      t2 = (t2 == 255) ? 255 : t2 + 1;
      printf("t1: %.2f, t2: %.2f, t3: %.2f\n", t1, t2, t3);
      break;
    case GLUT_KEY_F4:
      t2 = (t2 == 0.0) ? 0.0 : t2 - 1;
      printf("t1: %.2f, t2: %.2f, t3: %.2f\n", t1, t2, t3);
      break;
    case GLUT_KEY_F5:
      t3 = (t3 == 255) ? 255 : t3 + 1;
      printf("t1: %.2f, t2: %.2f, t3: %.2f\n", t1, t2, t3);
      break;
    case GLUT_KEY_F6:
      printf("t1: %.2f, t2: %.2f, t3: %.2f\n", t1, t2, t3);
      t3 = (t3 == 0.0) ? 0.0 : t3 - 1;
      break;
  }
}

void KeyEventSpecial(int key, int x, int y) { KeyEvent(key, x, y); }

int main(int argc, char** argv) {
  ComputeTransferFunction();
  volumeData = new cVolumeData("volumeData", 2);
  int height = volumeData->GetHeight();
  int width = volumeData->GetWidth();

  glutInit(&argc, argv);
  glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH | GLUT_MULTISAMPLE);
  glutInitWindowSize(width, height);
  glutCreateWindow("CAV Assignment 2 [BUILD: " __DATE__ "]");

  glClearColor(0.5, 0.5, 0.5, 1.0);

  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  glOrtho(0, width, height, 0, -512, 512);

  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity();

  glDisable(GL_DEPTH_TEST);

  glutKeyboardFunc(KeyEvent);
  glutSpecialFunc(KeyEventSpecial);
  glutDisplayFunc(Draw);
  glutIdleFunc(Update);

  glutMainLoop();

  delete volumeData;
};
