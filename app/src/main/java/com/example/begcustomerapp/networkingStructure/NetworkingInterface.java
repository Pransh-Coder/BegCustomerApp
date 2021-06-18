package com.example.begcustomerapp.networkingStructure;

import androidx.annotation.Nullable;

public interface NetworkingInterface {

    <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error);

    <T> void networkingRequestPerformed(@Nullable MethodType type, boolean status, @Nullable T error, Object o);

    enum MethodType {
        login, signUp, bookAppointment,addPatient;
    }
}
