package com.mojang.authlib;

public final class SignatureState {
    public static final SignatureState SIGNED = new SignatureState("signed");
    public static final SignatureState UNSIGNED = new SignatureState("unsigned");

    private final String name;
    public SignatureState(String name) { this.name = name; }
    public String name() { return name; }
}
