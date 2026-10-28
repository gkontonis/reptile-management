import { Component, HostListener, input, isDevMode, output } from '@angular/core';

export interface PrototypeVariantOption {
  key: string;
  label: string;
}

/**
 * PROTOTYPE INFRASTRUCTURE — not for production use.
 *
 * Floating bottom-centre bar for flipping between UI prototype variants
 * rendered on the same route via a `?variant=` query param. Hidden
 * automatically outside dev mode so a stray prototype merge can't ship it.
 *
 * Usage:
 * <app-prototype-variant-switcher
 *   [variants]="[{ key: 'A', label: 'Flat sections' }, { key: 'B', label: 'Flat panels' }]"
 *   [current]="variant()"
 *   (variantChange)="setVariant($event)"
 * />
 */
@Component({
  selector: 'app-prototype-variant-switcher',
  standalone: true,
  imports: [],
  templateUrl: './prototype-variant-switcher.component.html'
})
export class PrototypeVariantSwitcherComponent {
  variants = input<PrototypeVariantOption[]>([]);
  current = input<string>('');
  variantChange = output<string>();

  protected readonly devMode = isDevMode();

  @HostListener('document:keydown', ['$event'])
  onKeydown(event: KeyboardEvent): void {
    const target = event.target as HTMLElement | null;
    const tag = target?.tagName;
    if (tag === 'INPUT' || tag === 'TEXTAREA' || target?.isContentEditable) {
      return;
    }
    if (event.key === 'ArrowLeft') {
      this.cycle(-1);
    } else if (event.key === 'ArrowRight') {
      this.cycle(1);
    }
  }

  cycle(direction: 1 | -1): void {
    const options = this.variants();
    if (options.length === 0) return;
    const index = options.findIndex(v => v.key === this.current());
    const nextIndex = (index + direction + options.length) % options.length;
    this.variantChange.emit(options[nextIndex].key);
  }

  select(key: string): void {
    this.variantChange.emit(key);
  }
}
