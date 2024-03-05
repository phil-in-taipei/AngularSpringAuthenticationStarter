export interface AuthLoginModel {
    username: string;
    password: string;
  }

export interface AuthLoginResponseModel {
    refresh: string;
    token: string;
};

export interface AuthLoginResponseFailureModel {
  message: string;
};

export interface AuthTokenRefreshResponseModel {
  token: string;
};