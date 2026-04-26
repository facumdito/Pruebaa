#!/usr/bin/env python3
"""
AVM training script — Rosario Real Estate SaaS, Phase 8.
Reads listing data from CSV, trains GBM pipeline, exports to PMML via sklearn2pmml.

Dependencies:
    pip install pandas scikit-learn sklearn2pmml lightgbm

Usage:
    python train_avm.py --data listings.csv --output ../resources/avm_rosario_v1.pmml
"""

import argparse
import math
import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import OneHotEncoder
from sklearn.compose import ColumnTransformer
from sklearn.pipeline import Pipeline
from sklearn.ensemble import GradientBoostingRegressor
from sklearn.metrics import mean_absolute_percentage_error
from sklearn2pmml import sklearn2pmml
from sklearn2pmml.pipeline import PMMLPipeline


NUMERIC_FEATURES = [
    "m2_cubiertos", "m2_totales", "ambientes", "dormitorios",
    "banos", "cocheras", "antiguedad_anios", "lat", "lon"
]
CATEGORICAL_FEATURES = ["tipo", "barrio"]
TARGET = "precio_usd"


def load_data(path: str) -> pd.DataFrame:
    df = pd.read_csv(path)
    df = df.dropna(subset=[TARGET, "m2_cubiertos"])
    df = df[df[TARGET] > 0]
    return df


def train(df: pd.DataFrame) -> tuple:
    df["log_precio_usd"] = np.log(df[TARGET])

    X = df[NUMERIC_FEATURES + CATEGORICAL_FEATURES].copy()
    y = df["log_precio_usd"]

    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.15, random_state=42)

    cat_enc = OneHotEncoder(handle_unknown="ignore", sparse_output=False)
    preprocessor = ColumnTransformer([
        ("num", "passthrough", NUMERIC_FEATURES),
        ("cat", cat_enc, CATEGORICAL_FEATURES),
    ])

    gbm = GradientBoostingRegressor(
        n_estimators=400, learning_rate=0.05, max_depth=5,
        subsample=0.8, random_state=42
    )

    pipeline = PMMLPipeline([
        ("prep", preprocessor),
        ("model", gbm),
    ])
    pipeline.fit(X_train, y_train)

    # Metrics on held-out test set
    y_pred = pipeline.predict(X_test)
    errors = np.abs(np.exp(y_pred) - np.exp(y_test)) / np.exp(y_test)
    mdape = float(np.median(errors))
    pe10  = float(np.mean(errors <= 0.10))
    pe20  = float(np.mean(errors <= 0.20))

    print(f"MdAPE={mdape:.3f}  PE10={pe10:.3f}  PE20={pe20:.3f}  n_test={len(y_test)}")
    return pipeline, mdape, pe10, pe20


def export_pmml(pipeline, output_path: str) -> None:
    sklearn2pmml(pipeline, output_path, with_repr=True)
    print(f"PMML written → {output_path}")


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--data",   required=True,  help="Path to listings CSV")
    parser.add_argument("--output", required=True,  help="Output PMML file path")
    args = parser.parse_args()

    df = load_data(args.data)
    print(f"Loaded {len(df)} rows from {args.data}")

    pipeline, mdape, pe10, pe20 = train(df)
    export_pmml(pipeline, args.output)


if __name__ == "__main__":
    main()
