#include <stdlib.h>
#include <fstream>
#include <iostream>

#include "vol.h"

cVolumeData::cVolumeData(const std::string& filename, int resolution) {

  std::ifstream f(filename.c_str());
    if (f == NULL) {
      printf("Failed to read file %s\n", filename.c_str()); fflush(stdout);
      exit(EXIT_FAILURE);
    }
    
    f >> m_prev_width; f >> m_prev_height; f >> m_prev_depth;

    m_prev_data = new unsigned char[m_prev_width * m_prev_height * m_prev_depth];
    
    int val = 0;
    
    for(int z = 0; z < m_prev_depth; z++)
    for(int y = 0; y < m_prev_height; y++) 
    for(int x = 0; x < m_prev_width; x++) {
      f >> val;
      SetPrev(x, y, z, val);
    }
    
    f.close();

    // Interpolate based on resolution

    m_width = m_prev_width + (m_prev_width - 1) * (resolution - 1);
    m_height = m_prev_height + (m_prev_height - 1) * (resolution - 1);
    m_depth = m_prev_depth + (m_prev_depth - 1) * (resolution - 1);
    
    m_data = new unsigned char[m_width * m_height * m_depth];

    if (resolution == 1) {
      m_data = m_prev_data;
      printf("Volume Width: %i Height: %i Depth: %i\n", m_width, m_height, m_depth);
      return;
    }

    for (int z = 0; z < m_prev_depth; z++) {
      for (int y = 0; y < m_prev_height; y++) {
        for (int x = 0; x < m_prev_width; x++) {
          if(x == m_prev_width - 1)
            continue;
          // calculate the step with following value
          unsigned char val =  this->GetPrev(x, y, z);
          unsigned char next_val =  this->GetPrev(x+1, y, z);
          int step = (int)((next_val - val) / resolution);

          // assign intrapolated values
          for(int n = 0; n < resolution; n++) {
            int n_x = resolution * x + n;
            int n_val = val + (step * n);
            Set(n_x, y, z, n_val);
          }
        }
      }
    }
    for (int z = 0; z < m_prev_depth; z++) {
      for (int y = 0; y < m_prev_height; y++) {
        for (int x = 0; x < m_prev_width; x++) {
          if(y == m_prev_height - 1)
            continue;
          // calculate the step with following value
          unsigned char val =  this->GetPrev(x, y, z);
          unsigned char next_val =  this->GetPrev(x, y+1, z);
          int step = (int)((next_val - val) / resolution);

          // assign intrapolated values
          for(int n = 0; n < resolution; n++) {
            int n_y = resolution * y + n;
            int n_val = val + (step * n);
            Set(x, n_y, z, n_val);
          }
        }
      }
    }
    for (int z = 0; z < m_prev_depth; z++) {
      for (int y = 0; y < m_prev_height; y++) {
        for (int x = 0; x < m_prev_width; x++) {
          if(z == m_prev_depth - 1)
            continue;
          // calculate the step with following value
          unsigned char val =  this->GetPrev(x, y, z);
          unsigned char next_val =  this->GetPrev(x, y, z+1);
          int step = (int)((next_val - val) / resolution);

          // assign intrapolated values
          for(int n = 0; n < resolution; n++) {
            int n_z = resolution * z + n;
            int n_val = val + (step * n);
            Set(x, y, n_z, n_val);
          }
        }
      }
    }

    printf("Volume Width: %i Height: %i Depth: %i\n", m_width, m_height, m_depth);
}

cVolumeData::~cVolumeData(void) { delete[] m_data; }

int cVolumeData::GetDepth(void) const { return m_depth; }

int cVolumeData::GetWidth(void) const { return m_width; }

int cVolumeData::GetHeight(void) const { return m_height; }

void cVolumeData::Set(int x, int y, int z, unsigned char val) {
  int index = x + y * m_width + z * m_width * m_height;
  m_data[index] = val;
}

void cVolumeData::SetPrev(int x, int y, int z, unsigned char val) {
  int index = x + y * m_prev_width + z * m_prev_width * m_prev_height;
  m_prev_data[index] = val;
}

unsigned char cVolumeData::Get(int x, int y, int z) const {
  int index = x + y * m_width + z * m_width * m_height;
  return m_data[index];
}

unsigned char cVolumeData::GetPrev(int x, int y, int z) const {
  int index = x + y * m_prev_width + z * m_prev_width * m_prev_height;
  return m_prev_data[index];
}
