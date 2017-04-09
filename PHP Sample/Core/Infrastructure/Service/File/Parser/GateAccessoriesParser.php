<?php
declare(strict_types = 1);

namespace app\Core\Infrastructure\Service\File\Parser;

use app\Core\Infrastructure\Exception\CoreException;
use app\Core\Domain\Service\File\Parser;

final class GateAccessoriesParser implements Parser
{
    /**
     * @var array
     */
    private $vendorCodes = [];

    /**
     * @var array
     */
    private $resultArray = [];

    /**
     * GateTableParser constructor.
     * @param string $file
     * @throws CoreException
     */
    public function __construct(string $file)
    {
        if (file_exists($file)) {
            $this->parse($file);
        } else {
            throw new CoreException('Файл не существует.');
        }
    }

    /**
     * @param $file
     * @return array
     * @throws CoreException
     */
    private function parse(string $file)
    {
        $csvLines = file($file);
        $this->validateFileContent($csvLines);
        $array = [];
        foreach ($csvLines as $key => $item) {
            if ($key !== 0) {
                $lineArr = str_getcsv($item, ";");
                if (!$this->isNoValidCsvLine($lineArr)) {
                    $vendorCode = $this->normalizeString($lineArr[0]);
                    $name = $this->normalizeString($lineArr[1]);
                    $units = $this->normalizeString($lineArr[2]);
                    $weight = (float)$lineArr[3];
                    $type = $this->normalizeString($lineArr[4]);

                    $this->validateUnique($vendorCode);

                    array_push($array, [
                        'vendorCode' => $vendorCode,
                        'name' => $name,
                        'units' => $units,
                        'weight' => $weight,
                        'type' => $type
                    ]);
                }
            }
        }
        $this->resultArray = $array;
    }

    /**
     * @return array
     */
    public function getResultArray(): array
    {
        return $this->resultArray;
    }

    /**
     * Remove whitespaces from ends and center of string
     *
     * @param string $string
     * @return string
     */
    private function normalizeString(string $string): string
    {
        $string = trim($string);
        return preg_replace("/\s{2,}/", " ", $string);
    }

    /**
     * @param array $csvLineArr
     * @return bool
     */
    private function isNoValidCsvLine(array $csvLineArr): bool
    {
        return
            $csvLineArr[0] === '' || // vendorCode
            $csvLineArr[1] === '' || // name
            $csvLineArr[2] === '' || // units
            (float)$csvLineArr[3] === 0 && $csvLineArr[3] !== '0' || // weight
            $csvLineArr[2] === ''; // type
    }

    /**
     * @param string $vendorCode
     * @throws CoreException
     */
    private function validateUnique(string $vendorCode)
    {
        if (!in_array($vendorCode, $this->vendorCodes)) {
            $this->vendorCodes[] = $vendorCode;
        } else {
            throw new CoreException('Артикул должен быть уникальным. Несколько товаров с артикулом ' . $vendorCode);
        }
    }

    /**
     * @param array $csvLines
     * @throws CoreException
     */
    private function validateFileContent(array $csvLines)
    {
        $secondLineArr = str_getcsv($csvLines[1], ";");
        if ($this->isNoValidCsvLine($secondLineArr)) {
            throw new CoreException('Данные неверного формата.');
        }
    }
}