#pragma once

#include "vec.h"

class mat2 {
 public:
  float xx, xy;
  float yx, yy;

  mat2(void);
  mat2(float xx, float xy, float yx, float yy);
  mat2(const vec2& r1, const vec2& r2);

  static mat2 Id(void);
  static mat2 Zero(void);
  static mat2 Rotation(float a);
  static float Determinant(const mat2& m);

  static mat2 Inverse(const mat2& m);

  static void Print(const mat2& m);

  mat2 operator*(const mat2& m) const;
  vec2 operator*(const vec2& v) const;
};

class mat3 {
 public:
  float xx, xy, xz;
  float yx, yy, yz;
  float zx, zy, zz;

  mat3(void);
  mat3(float xx, float xy, float xz, float yx, float yy, float yz, float zx,
       float zy, float zz);
  mat3(const vec3& r1, const vec3& r2, const vec3& r3);

  static mat3 Id(void);
  static mat3 Zero(void);

  static mat3 RotationX(float a);
  static mat3 RotationY(float a);
  static mat3 RotationZ(float a);
  static mat3 RotationAngleAxis(const vec3& axis, float angle);

  static float Determinant(const mat3& m);
  static mat3 Inverse(const mat3& m);

  static void Print(const mat3& m);

  mat3 operator*(const mat3& m) const;
  vec3 operator*(const vec3& v) const;
};

class mat4 {
 public:
  float xx, xy, xz, xw;
  float yx, yy, yz, yw;
  float zx, zy, zz, zw;
  float wx, wy, wz, ww;

  mat4(void);
  mat4(float xx, float xy, float xz, float xw, float yx, float yy, float yz,
       float yw, float zx, float zy, float zz, float zw, float wx, float wy,
       float wz, float ww);
  mat4(const vec4& r1, const vec4& r2, const vec4& r3, const vec4& r4);

  static mat4 Id(void);
  static mat4 Zero(void);

  static mat4 RotationX(float a);
  static mat4 RotationY(float a);
  static mat4 RotationZ(float a);
  static mat4 RotationEuler(float a, float b, float c);
  static mat4 RotationAngleAxis(const vec3& axis, float angle);

  static mat4 Translation(const vec3& trans);
  static mat4 Scale(const vec3& scale);

  static mat4 ViewLookAt(const vec3& position, const vec3& target,
                         const vec3& up);
  static mat4 Perspective(float fov, float near, float far, float ratio);
  static mat4 Orthographic(float left, float right, float bottom, float top,
                           float near, float far);

  static mat4 FromMat3(const mat3& m);
  static mat3 ToMat3(const mat4& m);

  static mat4 Transpose(const mat4& m);
  static float Determinant(const mat4& m);
  static mat4 Inverse(const mat4& m);

  static void Print(const mat4& m);

  mat4 operator*(const mat4& m) const;
  mat4 operator*(const mat3& m) const;
  vec4 operator*(const vec4& v) const;
  vec3 operator*(const vec3& v) const;

  mat4 operator*(float fac) const;
  mat4 operator+(const mat4& m) const;
};