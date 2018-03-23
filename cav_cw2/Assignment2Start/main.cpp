#include <stdio.h>
#include <stdlib.h>

#include <GL/glut.h>
#define GLUT_KEY_ESCAPE 27
#ifndef GLUT_WHEEL_UP
#define GLUT_WHEEL_UP 3
#define GLUT_WHEEL_DOWN 4
#endif

#include "mat.h"
#include "vec.h"
#include "vol.h"

#define WIDTH 128                                           
#define HEIGHT 256

static cVolumeData* volumeData = NULL;
static unsigned char threshold = 75;

vec3 linearTF(vec3 a, vec3 b, float t) {
  return a * (1-t) + b * t;
}

void Update(void) { glutPostRedisplay(); }

void Draw(void) {
  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  glPointSize(2.0);
  glBegin(GL_POINTS);

  for (int z = 0; z < volumeData->GetDepth(); z++) {
    for (int y = 0; y < volumeData->GetHeight(); y++) {
      for (int x = 0; x < volumeData->GetWidth(); x++) {
        unsigned char val = volumeData->Get(x, y, z);

        // z, y, x are height, width and depth
        
        /* TODO:
        **
        **  Here is where you should calculate the color of
        **  the pixel via some more sophisticated method.
        */

        if (val > threshold) {
          vec3 red = vec3(1,0,0);
          vec3 blue = vec3(0,0,1);
          vec3 color = linearTF(red, blue, val/255.0);
          //vec3 color = vec3(val, val, val) / 255.0;
          glColor3f(color.r(), color.g(), color.b());
          glVertex3f(y, z, 0);
          break;
        }
      }
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
      threshold = (threshold == 255) ? 255 : threshold + 1;
      break;
    case GLUT_KEY_DOWN:
      threshold = (threshold == 0) ? 0 : threshold - 1;
      break;
  }
}

void KeyEventSpecial(int key, int x, int y) { KeyEvent(key, x, y); }

int main(int argc, char** argv) {
  volumeData = new cVolumeData("volumeData");

  glutInit(&argc, argv);
  glutInitDisplayMode(GLUT_RGB | GLUT_DOUBLE | GLUT_DEPTH | GLUT_MULTISAMPLE);
  glutInitWindowSize(WIDTH, HEIGHT);
  glutCreateWindow("CAV Assignment 2 [BUILD: " __DATE__ "]");

  glClearColor(0.5, 0.5, 0.5, 1.0);

  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  glOrtho(0, WIDTH, HEIGHT, 0, -512, 512);

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
