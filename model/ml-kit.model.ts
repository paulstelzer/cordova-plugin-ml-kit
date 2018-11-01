export interface MlKitText {
    text: string;
    textBlocks: MlKitTextBlock[];
}

export interface MlKitTextBlock {
    boundingBox: MlKitBoundingBox;
    lines: {
        boundingBox: MlKitBoundingBox;
        text: string;
    }
    text: string;
}

export interface MlKitBoundingBox {
    left: number;
    right: number;
    top: number;
    bottom: number;
}