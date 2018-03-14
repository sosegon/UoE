#pragma once

class vec2 {
 public:
  float x, y;

  vec2(void);
  vec2(float x, float y);

  static vec2 Zero(void);
  static vec2 One(void);

  static float Length(const vec2& v);
  static float Distance(const vec2& v1, const vec2& v2);
  static vec2 Normalize(const vec2& v);

  static float Dot(const vec2& v1, const vec2& v2);
  static vec2 Max(const vec2& v, float val);
  static vec2 Min(const vec2& v, float val);
  static vec2 Clamp(const vec2& v, float bottom, float top);

  static void Print(const vec2& v);

  float operator[](int i) const;

  bool operator==(const vec2& v) const;
  bool operator!=(const vec2& v) const;

  vec2 operator*(float factor) const;
  const vec2& operator*=(float factor);
  vec2 operator*(const vec2& v) const;
  const vec2& operator*=(const vec2& v);
  vec2 operator/(float factor) const;
  vec2 operator/(const vec2& v) const;
  vec2 operator+(float factor) const;
  vec2 operator+(const vec2& v) const;
  const vec2& operator+=(const vec2& v);
  vec2 operator-(const vec2& v) const;
  const vec2& operator-=(const vec2& v);
  const vec2& operator-=(float factor);
  vec2 operator-(void) const;
};

class vec3 {
 public:
  float x, y, z;

  vec3(void);
  vec3(float x, float y, float z);

  static vec3 Zero(void);
  static vec3 One(void);

  static float Length(const vec3& v);
  static float Distance(const vec3& v1, const vec3& v2);
  static vec3 Normalize(const vec3& v);

  static float Dot(const vec3& v1, const vec3& v2);
  static vec3 Cross(const vec3& v1, const vec3& v2);

  static vec3 Max(const vec3& v, float val);
  static vec3 Min(const vec3& v, float val);
  static vec3 Clamp(const vec3& v, float bottom, float top);

  static void Print(const vec3& v);

  float r(void) const;
  float g(void) const;
  float b(void) const;

  vec2 xy(void) const;

  float operator[](int i) const;

  bool operator==(const vec3& v) const;
  bool operator!=(const vec3& v) const;

  vec3 operator*(float factor) const;
  const vec3& operator*=(float factor);
  vec3 operator*(const vec3& v) const;
  const vec3& operator*=(const vec3& v);
  vec3 operator/(float factor) const;
  vec3 operator/(const vec3& v) const;
  vec3 operator+(const vec3& v) const;
  vec3 operator+(float factor) const;
  const vec3& operator+=(const vec3& v);
  vec3 operator-(const vec3& v) const;
  const vec3& operator-=(const vec3& v);
  const vec3& operator-=(float factor);
  vec3 operator-(void) const;
};

class vec4 {
 public:
  float x, y, z, w;

  vec4(void);
  vec4(float x, float y, float z, float w);
  vec4(const vec2& v, float z, float w);
  vec4(const vec3& v, float w);

  static vec4 Zero(void);
  static vec4 One(void);

  static float Length(const vec4& v);
  static vec4 Normalize(const vec4& v);

  static float Dot(const vec4& v1, const vec4& v2);

  static vec4 Max(const vec4& v, float val);
  static vec4 Min(const vec4& v, float val);
  static vec4 Clamp(const vec4& v, float bottom, float top);

  static void Print(const vec4& v);

  static vec3 FromHomogeneous(const vec4& v);
  static vec4 ToHomogeneous(const vec3& v);

  float r(void) const;
  float g(void) const;
  float b(void) const;
  float a(void) const;

  vec3 xyz(void) const;
  vec3 rgb(void) const;

  float operator[](int i) const;

  bool operator==(const vec4& v) const;
  bool operator!=(const vec4& v) const;

  vec4 operator*(float factor) const;
  const vec4& operator*=(float factor);
  vec4 operator*(const vec4& v) const;
  const vec4& operator*=(const vec4& v);
  vec4 operator/(float factor) const;
  vec4 operator/(const vec4& v) const;
  vec4 operator+(float factor) const;
  vec4 operator+(const vec4& v) const;
  const vec4& operator+=(const vec4& v);
  vec4 operator-(const vec4& v) const;
  const vec4& operator-=(const vec4& v);
  const vec4& operator-=(float factor);
  vec4 operator-(void) const;
};
