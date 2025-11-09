"""Utility per aggiungere commenti descrittivi a ogni riga di codice Java."""

from __future__ import annotations

import re
from pathlib import Path


BASE_DIR = Path(__file__).resolve().parents[1] / "server" / "src" / "main" / "java"


def has_code_comment(line: str) -> bool:
    """Verifica se la riga contiene già un commento di codice."""

    stripped = line.lstrip()
    if not stripped:
        return True
    if stripped.startswith("//") or stripped.startswith("/*") or stripped.startswith("*"):
        return True
    return "//" in stripped


def annotate_line(line: str, current_class: str | None) -> tuple[str, str | None]:
    """Restituisce la riga annotata e l'eventuale nome della classe corrente."""

    if has_code_comment(line):
        return line, current_class

    stripped = line.strip()
    if not stripped:
        return line, current_class

    indent = line[: len(line) - len(line.lstrip())]
    comment = "// Esegue l'istruzione necessaria alla logica applicativa."

    if stripped.startswith("package ") and stripped.endswith(";"):
        pkg = stripped[len("package ") : -1].strip()
        comment = f"// Definisce il pacchetto {pkg} che contiene questa classe."
    elif stripped.startswith("import ") and stripped.endswith(";"):
        imp = stripped[len("import ") : -1].strip()
        comment = f"// Importa {imp} per abilitare le funzionalità utilizzate nel file."
    elif stripped.startswith("@"):  # annotazione
        annotation = stripped[1:].split("(")[0]
        comment = f"// Applica l'annotazione @{annotation} per configurare il componente."
    else:
        handled = False
        class_match = re.match(
            r"(public|protected|private)?\s*(final\s+)?(class|interface|enum|record)\s+(\w+)",
            stripped,
        )
        if class_match:
            kind = class_match.group(3)
            kind_map = {
                "class": "classe",
                "interface": "interfaccia",
                "enum": "enumerazione",
                "record": "record",
            }
            current_class = class_match.group(4)
            comment = (
                f"// Definisce la {kind_map.get(kind, kind)} {current_class} che incapsula la logica applicativa."
            )
            handled = True
        if not handled:
            field_match = re.match(r"(public|protected|private)\s+[^{;]+;", stripped)
            if field_match and "(" not in stripped:
                name_part = stripped.rstrip(";").split()[-1]
                comment = f"// Dichiara il campo {name_part} dell'oggetto."
                handled = True
        if not handled:
            method_match = re.match(
                r"(public|protected|private|static|default)\b[^{;]*\(",
                stripped,
            )
            if method_match:
                name = re.split(r"\s+", stripped.split("(")[0].strip())[-1]
                if current_class and name == current_class:
                    comment = (
                        f"// Costruttore della classe {current_class} che inizializza le dipendenze necessarie."
                    )
                else:
                    comment = f"// Definisce il metodo {name} che supporta la logica di dominio."
                handled = True
        if not handled and stripped.startswith("return "):
            expr = stripped[len("return ") :].rstrip(";")
            comment = f"// Restituisce il risultato dell'espressione {expr}."
            handled = True
        if not handled and stripped.startswith("throw "):
            comment = "// Propaga un'eccezione verso il chiamante."
            handled = True
        if not handled and (stripped.startswith("if ") or stripped.startswith("if(")):
            comment = "// Valuta la condizione per controllare il flusso applicativo."
            handled = True
        if not handled and stripped.startswith("else"):
            comment = "// Gestisce il ramo alternativo della condizione."
            handled = True
        if not handled and stripped.startswith("switch"):
            comment = "// Seleziona il ramo logico in base al valore indicato."
            handled = True
        if not handled and stripped.startswith("case "):
            comment = "// Gestisce uno dei possibili casi nello switch."
            handled = True
        if not handled and stripped.startswith("default"):
            comment = "// Gestisce il caso predefinito nello switch."
            handled = True
        if not handled and (stripped.startswith("for ") or stripped.startswith("for(")):
            comment = "// Itera sugli elementi richiesti dalla logica."
            handled = True
        if not handled and (stripped.startswith("while ") or stripped.startswith("while(")):
            comment = "// Ripete l'esecuzione finché la condizione rimane vera."
            handled = True
        if not handled and stripped.startswith("do "):
            comment = "// Avvia un ciclo do-while per ripetere le istruzioni successive."
            handled = True
        if not handled and stripped.startswith("try"):
            comment = "// Avvia il blocco protetto per intercettare eventuali eccezioni."
            handled = True
        if not handled and stripped.startswith("catch"):
            comment = "// Intercetta e gestisce l'eccezione generata nel blocco try."
            handled = True
        if not handled and stripped.startswith("finally"):
            comment = "// Esegue il blocco finale indipendentemente dagli esiti precedenti."
            handled = True
        if not handled and stripped.startswith("break"):
            comment = "// Interrompe il ciclo o lo switch corrente."
            handled = True
        if not handled and stripped.startswith("continue"):
            comment = "// Passa direttamente all'iterazione successiva del ciclo."
            handled = True
        if not handled and stripped.startswith("new "):
            comment = "// Istanzia un nuovo oggetto necessario alla logica applicativa."
            handled = True
        if not handled and stripped.startswith("this."):
            field = stripped[len("this.") :].split("=")[0].strip()
            comment = f"// Aggiorna il campo {field} dell'istanza."
            handled = True
        if not handled and stripped.startswith("super("):
            comment = "// Invoca il costruttore della superclasse per inizializzare lo stato ereditato."
            handled = True
        if not handled and " = " in stripped and stripped.endswith(";"):
            target = stripped.split("=")[0].strip()
            comment = f"// Assegna il valore calcolato alla variabile {target}."
            handled = True
        if not handled and (stripped == "{" or stripped.endswith("{")):
            comment = "// Apre il blocco di codice associato alla dichiarazione."
            handled = True
        if not handled and (stripped == "}" or stripped == "};" or stripped.startswith("}")):
            comment = "// Chiude il blocco di codice precedente."
            handled = True
        if not handled and stripped.endswith(":") and not stripped.startswith("case"):
            comment = "// Etichetta utilizzata per il flusso di controllo."
            handled = True
        if not handled and stripped.endswith(";"):
            comment = "// Esegue l'istruzione terminata dal punto e virgola."

    new_line = f"{indent}{stripped} {comment}\n"
    return new_line, current_class


def process_file(path: Path) -> None:
    """Applica i commenti descrittivi al file specificato."""

    current_class: str | None = None
    updated_lines: list[str] = []
    changed = False

    lines = path.read_text(encoding="utf-8").splitlines(keepends=True)
    for line in lines:
        new_line, current_class = annotate_line(line, current_class)
        updated_lines.append(new_line)
        if new_line != line:
            changed = True

    if changed:
        path.write_text("".join(updated_lines), encoding="utf-8")


def main() -> None:
    for java_file in BASE_DIR.rglob("*.java"):
        process_file(java_file)


if __name__ == "__main__":
    main()
