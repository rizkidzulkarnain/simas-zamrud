package it.starksoftware.ssform.model;

import it.starksoftware.ssform.interfaces.ButtonCallBack;


public class FormElementButton implements FormObject {

    // private variables
    private int mTag; // unique tag to identify the object
    private int mType; // type for the form element
    private String mTitle; // title to be shown on left
    private boolean mValue; // value to be shown on right

    private boolean mValueCheckA;
    private boolean mValueCheckB;
    private boolean visibility = true;
    private boolean required = false;

    private ButtonCallBack buttonCallBack;
    private String mAttribute;

    public FormElementButton() {
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    public ButtonCallBack getButtonCallBack() {
        return buttonCallBack;
    }

    public FormElementButton setButtonCallBack(ButtonCallBack buttonCallBack) {
        this.buttonCallBack = buttonCallBack;
        return this;
    }

    public static FormElementButton createInstance() {
        return new FormElementButton();
    }

    public FormElementButton setVisibility(boolean visibility) {
        this.visibility = visibility;
        return this;
    }
    public boolean getVisibility() {
        return visibility;
    }

    // getters and setters
    public FormElementButton setTag(int mTag) {
        this.mTag = mTag;
        return this;
    }

    public FormElementButton setType(int mType) {
        this.mType = mType;
        return this;
    }

    public FormElementButton setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public FormElementButton setValue(boolean mValue) {
        this.mValue = mValue;
        return this;
    }

    public FormElementButton setValueCheckA(boolean mValue) {
        this.mValueCheckA = mValue;
        return this;
    }

    public FormElementButton setValueCheckB(boolean mValue) {
        this.mValueCheckB = mValue;
        return this;
    }

    public FormElementButton setAttribute(String attribute) {
        this.mAttribute = attribute;
        return this;
    }

    public String getAttribute() {
        return mAttribute;
    }

    public int getTag() {
        return mTag;
    }

    public int getType() {
        return mType;
    }

    public String getTitle() {
        return mTitle;
    }

    public boolean getValue() {
        return mValue;
    }

    @Override
    public boolean isHeader() {
        return false;
    }

    @Override
    public String getElementType() {

        return "Button";
    }

    @Override
    public String toString() {
        return "TAG: " + String.valueOf(this.mTag) + ", TITLE: " + this.mTitle + ", VALUE: " + this.mValue ;
    }
}
