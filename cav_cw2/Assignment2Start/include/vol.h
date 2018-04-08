#pragma once

#include <string>

class cVolumeData {
public:
  cVolumeData(const std::string& filename, const int resolution);
  ~cVolumeData(void);

  int GetWidth(void) const;
  int GetHeight(void) const;
  int GetDepth(void) const;

  void Set(int x, int y, int z, unsigned char amount);
  void SetPrev(int x, int y, int z, unsigned char amount);
  unsigned char Get(int x, int y, int z) const;
  unsigned char GetPrev(int x, int y, int z) const;

private:
  int m_width;
  int m_height;
  int m_depth;
  int m_prev_width;
  int m_prev_height;
  int m_prev_depth;

  unsigned char* m_data;
  unsigned char* m_prev_data;
};
