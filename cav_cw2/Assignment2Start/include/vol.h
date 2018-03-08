#pragma once

#include <string>

class cVolumeData {
public:
  cVolumeData(const std::string& filename);
  ~cVolumeData(void);

  int GetWidth(void) const;
  int GetHeight(void) const;
  int GetDepth(void) const;

  void Set(int x, int y, int z, unsigned char amount);
  unsigned char Get(int x, int y, int z) const;

private:
  int m_width;
  int m_height;
  int m_depth;

  unsigned char* m_data;
};
