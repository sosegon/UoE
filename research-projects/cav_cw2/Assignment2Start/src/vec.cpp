#include "vec.h"

#include <stdio.h>
#include <cmath>

vec2::vec2(void) : x(0.0f), y(0.0f){};

vec2::vec2(float x, float y) : x(x), y(y){};

vec2 vec2::One(void) { return vec2(1.0, 1.0); }

vec2 vec2::Zero(void) { return vec2(0.0, 0.0); }

float vec2::Length(const vec2& v) { return sqrt(v.x * v.x + v.y * v.y); }

float vec2::Distance(const vec2& v1, const vec2& v2) {
  return Length(v1 - v2);
}

vec2 vec2::Normalize(const vec2& v) {
  float l = Length(v);
  return vec2(v.x / l, v.y / l);
}

float vec2::Dot(const vec2& v1, const vec2& v2) {
  return v1.x * v2.x + v1.y * v2.y;
}

vec2 vec2::Max(const vec2& v, float val) {
  return vec2(v.x > val ? val : v.x, v.y > val ? val : v.y);
}

vec2 vec2::Min(const vec2& v, float val) {
  return vec2(v.x < val ? v.x : val, v.y < val ? v.y : val);
}

vec2 vec2::Clamp(const vec2& v, float bottom, float top) {
  vec2 ret;
  ret = vec2::Max(v, bottom);
  ret = vec2::Min(v, top);
  return ret;
}

void vec2::Print(const vec2& v) {
  printf("vec2(%0.2f, %0.2f)", v.x, v.y);
}

float vec2::operator[](int i) const {
  if (i == 0) {
    return x;
  } else if (i == 1) {
    return y;
  } else {
    printf("Invalid vec2 Subscript: %i\n", i);
    return 0;
  }
}

bool vec2::operator==(const vec2& v) const {
  return (x == v.x) && (y == v.y);
}

bool vec2::operator!=(const vec2& v) const {
  return (x != v.x) || (y != v.y);
}

vec2 vec2::operator*(float factor) const {
  return vec2(x * factor, y * factor);
}

const vec2& vec2::operator*=(float factor) {
  x *= factor;
  y *= factor;
  return *this;
}

vec2 vec2::operator*(const vec2& v) const {
  return vec2(x * v.x, y * v.y);
}

const vec2& vec2::operator*=(const vec2& v) {
  x *= v.x;
  y *= v.y;
  return *this;
}

vec2 vec2::operator/(float factor) const {
  return vec2(x / factor, y / factor);
}

vec2 vec2::operator/(const vec2& v) const {
  return vec2(x / v.x, y / v.y);
}

vec2 vec2::operator+(float factor) const {
  return vec2(x + factor, y + factor);
}

vec2 vec2::operator+(const vec2& v) const {
  return vec2(x + v.x, y + v.y);
}

const vec2& vec2::operator+=(const vec2& v) {
  x += v.x;
  y += v.y;
  return *this;
}

vec2 vec2::operator-(const vec2& v) const {
  return vec2(x - v.x, y - v.y);
}

const vec2& vec2::operator-=(const vec2& v) {
  x -= v.x;
  y -= v.y;
  return *this;
}

const vec2& vec2::operator-=(float factor) {
  x -= factor;
  y -= factor;
  return *this;
}

vec2 vec2::operator-(void) const { return vec2(-x, -y); }

vec3::vec3(void) : x(0.0f), y(0.0f), z(0.0f){};

vec3::vec3(float x, float y, float z) : x(x), y(y), z(z){};

vec3 vec3::One(void) { return vec3(1.0, 1.0, 1.0); }

vec3 vec3::Zero(void) { return vec3(0.0, 0.0, 0.0); }

float vec3::r(void) const { return x; }

float vec3::g(void) const { return y; }

float vec3::b(void) const { return z; }

float vec3::Length(const vec3& v) {
  return sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
}

float vec3::Distance(const vec3& v1, const vec3& v2) {
  return Length(v1 - v2);
}

vec3 vec3::Normalize(const vec3& v) {
  float l = Length(v);
  return vec3(v.x, v.y, v.z) / l;
}

float vec3::Dot(const vec3& v1, const vec3& v2) {
  return (v1.x * v2.x + v1.y * v2.y + v1.z * v2.z);
}

vec3 vec3::Cross(const vec3& v1, const vec3& v2) {
  return vec3(v1.y * v2.z - v1.z * v2.y, v1.z * v2.x - v1.x * v2.z,
                 v1.x * v2.y - v1.y * v2.x);
}

vec3 vec3::Max(const vec3& v, float val) {
  return vec3(v.x > val ? val : v.x, v.y > val ? val : v.y,
                 v.z > val ? val : v.z);
}

vec3 vec3::Min(const vec3& v, float val) {
  return vec3(v.x < val ? v.x : val, v.y < val ? v.y : val,
                 v.z < val ? v.z : val);
}

vec3 vec3::Clamp(const vec3& v, float bottom, float top) {
  vec3 ret;
  ret = vec3::Max(v, bottom);
  ret = vec3::Min(v, top);
  return ret;
}

void vec3::Print(const vec3& v) {
  printf("vec3(%0.2f, %0.2f, %0.2f)", v.x, v.y, v.z);
}

float vec3::operator[](int i) const {
  if (i == 0) {
    return x;
  } else if (i == 1) {
    return y;
  } else if (i == 2) {
    return z;
  } else {
    printf("Invalid vec3 Subscript: %i\n", i);
    return 0;
  }
}

bool vec3::operator==(const vec3& v) const {
  return (x == v.x) && (y == v.y) && (z == v.z);
}

bool vec3::operator!=(const vec3& v) const {
  return (x != v.x) || (y != v.y) || (z != v.z);
}

vec3 vec3::operator*(float factor) const {
  return vec3(x * factor, y * factor, z * factor);
}

const vec3& vec3::operator*=(float factor) {
  x *= factor;
  y *= factor;
  z *= factor;
  return *this;
}

vec3 vec3::operator*(const vec3& v) const {
  return vec3(x * v.x, y * v.y, z * v.z);
}

const vec3& vec3::operator*=(const vec3& v) {
  x *= v.x;
  y *= v.y;
  z *= v.z;
  return *this;
}

vec3 vec3::operator/(float factor) const {
  return vec3(x / factor, y / factor, z / factor);
}

vec3 vec3::operator/(const vec3& v) const {
  return vec3(x / v.x, y / v.y, z / v.z);
}

vec3 vec3::operator+(const vec3& v) const {
  return vec3(x + v.x, y + v.y, z + v.z);
}

vec3 vec3::operator+(float factor) const {
  return vec3(x + factor, y + factor, z + factor);
}

const vec3& vec3::operator+=(const vec3& v) {
  x += v.x;
  y += v.y;
  z += v.z;
  return *this;
}

vec3 vec3::operator-(const vec3& v) const {
  return vec3(x - v.x, y - v.y, z - v.z);
}

const vec3& vec3::operator-=(const vec3& v) {
  x -= v.x;
  y -= v.y;
  z -= v.z;
  return *this;
}

const vec3& vec3::operator-=(float factor) {
  x -= factor;
  y -= factor;
  z -= factor;
  return *this;
}

vec3 vec3::operator-(void) const { return vec3(-x, -y, -z); }

vec2 vec3::xy(void) const { return vec2(x, y); }

vec4::vec4(void) : x(0.0f), y(0.0f), z(0.0f), w(0.0f) {}

vec4::vec4(float x, float y, float z, float w) : x(x), y(y), z(z), w(w) {}

vec4::vec4(const vec2& v, float z, float w)
    : x(v.x), y(v.y), z(z), w(w) {}

vec4::vec4(const vec3& v, float w) : x(v.x), y(v.y), z(v.z), w(w) {}

vec4 vec4::One(void) { return vec4(1.0, 1.0, 1.0, 1.0); }

vec4 vec4::Zero(void) { return vec4(0.0, 0.0, 0.0, 0.0); }

float vec4::Length(const vec4& v) {
  return sqrt(v.x * v.x + v.y * v.y + v.z * v.z + v.w * v.w);
}

vec4 vec4::Normalize(const vec4& v) { return v / vec4::Length(v); }

float vec4::Dot(const vec4& v1, const vec4& v2) {
  return (v1.x * v2.x + v1.y * v2.y + v1.z * v2.z + v1.w * v2.w);
}

void vec4::Print(const vec4& v) {
  printf("vec4(%0.2f, %0.2f, %0.2f, %0.2f)", v.x, v.y, v.z, v.w);
}

float vec4::r(void) const { return x; }

float vec4::g(void) const { return y; }

float vec4::b(void) const { return z; }

float vec4::a(void) const { return w; }

vec3 vec4::xyz(void) const { return vec3(x, y, z); }

vec3 vec4::rgb(void) const { return vec3(x, y, z); }

vec4 vec4::Max(const vec4& v, float val) {
  return vec4(v.x > val ? val : v.x, v.y > val ? val : v.y,
                 v.z > val ? val : v.z, v.w > val ? val : v.w);
}

vec4 vec4::Min(const vec4& v, float val) {
  return vec4(v.x < val ? v.x : val, v.y < val ? v.y : val,
                 v.z < val ? v.z : val, v.w < val ? v.w : val);
}

vec4 vec4::Clamp(const vec4& v, float bottom, float top) {
  vec4 ret;
  ret = vec4::Max(v, bottom);
  ret = vec4::Min(v, top);
  return ret;
}

vec3 vec4::FromHomogeneous(const vec4& v) {
  return vec3(v.x, v.y, v.z) / v.w;
}

vec4 vec4::ToHomogeneous(const vec3& v) {
  return vec4(v.x, v.y, v.z, 1.0);
}

float vec4::operator[](int i) const {
  if (i == 0) {
    return x;
  } else if (i == 1) {
    return y;
  } else if (i == 2) {
    return z;
  } else if (i == 3) {
    return w;
  } else {
    printf("Invalid vec4 Subscript: %i\n", i);
    return 0;
  }
}

bool vec4::operator==(const vec4& v) const {
  return (x == v.x) && (y == v.y) && (z == v.z) && (w == v.w);
}

bool vec4::operator!=(const vec4& v) const {
  return (x != v.x) || (y != v.y) || (z != v.z) || (w != v.w);
}

vec4 vec4::operator*(float factor) const {
  return vec4(x * factor, y * factor, z * factor, w * factor);
}

const vec4& vec4::operator*=(float factor) {
  x *= factor;
  y *= factor;
  z *= factor;
  w *= factor;
  return *this;
}

vec4 vec4::operator*(const vec4& v) const {
  return vec4(x * v.x, y * v.y, z * v.z, w * v.w);
}

const vec4& vec4::operator*=(const vec4& v) {
  x *= v.x;
  y *= v.y;
  z *= v.z;
  w *= v.w;
  return *this;
}

vec4 vec4::operator/(float factor) const {
  return vec4(x / factor, y / factor, z / factor, w / factor);
}

vec4 vec4::operator/(const vec4& v) const {
  return vec4(x / v.x, y / v.y, z / v.z, w / v.w);
}

vec4 vec4::operator+(float factor) const {
  return vec4(x + factor, y + factor, z + factor, w + factor);
}

vec4 vec4::operator+(const vec4& v) const {
  return vec4(x + v.x, y + v.y, z + v.z, w + v.w);
}

const vec4& vec4::operator+=(const vec4& v) {
  x += v.x;
  y += v.y;
  z += v.z;
  w += v.w;
  return *this;
}

vec4 vec4::operator-(const vec4& v) const {
  return vec4(x - v.x, y - v.y, z - v.z, w - v.w);
}

const vec4& vec4::operator-=(const vec4& v) {
  x -= v.x;
  y -= v.y;
  z -= v.z;
  w -= v.w;
  return *this;
}

const vec4& vec4::operator-=(float factor) {
  x -= factor;
  y -= factor;
  z -= factor;
  w -= factor;
  return *this;
}

vec4 vec4::operator-(void) const { return vec4(-x, -y, -z, -w); }
